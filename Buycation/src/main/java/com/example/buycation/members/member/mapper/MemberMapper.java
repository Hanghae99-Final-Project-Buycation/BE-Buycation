package com.example.buycation.members.member.mapper;

import com.example.buycation.members.member.dto.SignupRequestDto;
import com.example.buycation.members.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberMapper {
    private final PasswordEncoder passwordEncoder;
    public Member toMember(SignupRequestDto signupRequestDto) {
        return Member.builder()
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .nickname(signupRequestDto.getNickname())
                .address(signupRequestDto.getAddress())
                .build();
    }
}
