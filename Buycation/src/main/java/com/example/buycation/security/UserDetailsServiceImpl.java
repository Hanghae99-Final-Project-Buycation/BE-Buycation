package com.example.buycation.security;

import com.example.buycation.common.exception.CustomException;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.members.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.buycation.common.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        return new UserDetailsImpl(member, member.getEmail());
    }
}