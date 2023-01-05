package com.example.buycation.members.member.service;

import com.example.buycation.common.exception.CustomException;
import com.example.buycation.members.member.dto.LoginRequestDto;
import com.example.buycation.members.member.dto.SignupRequestDto;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.members.member.repository.MemberRepository;
import com.example.buycation.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.example.buycation.common.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {

        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String nickname = signupRequestDto.getNickname();
        String address = signupRequestDto.getAddress();

        // 로그인 ID 중복 확인
        Optional<Member> emailDuplicateCheck = memberRepository.findByEmail(email);
        if(emailDuplicateCheck.isPresent()) {
            throw new CustomException(DUPLICATE_EMAIL);
        }

        // 닉네임 중복 확인
        Optional<Member> nicknameDuplicateCheck = memberRepository.findByNickname(nickname);
        if (nicknameDuplicateCheck.isPresent()) {
            throw new CustomException(DUPLICATE_NICKNAME);
        }

        Member member = new Member(email, password, nickname, address);
        memberRepository.save(member);
    }

    @Transactional
    public void login(LoginRequestDto loginRequestDto, HttpServletResponse response){

        String inputEmail = loginRequestDto.getEmail();
        String inputPassword = loginRequestDto.getPassword();

        Member member = memberRepository.findByEmail(inputEmail).orElseThrow(
                () -> new CustomException(USERNAME_NOT_FOUND)
        );

        if(!passwordEncoder.matches(inputPassword, member.getPassword())){
            throw new CustomException(INCORRECT_PASSWORD);
        }
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(member.getEmail()));
    }
}