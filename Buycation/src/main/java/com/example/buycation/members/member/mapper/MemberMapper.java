package com.example.buycation.members.member.mapper;

import com.example.buycation.members.member.dto.LoginResponseDto;
import com.example.buycation.members.member.dto.MemberResponseDto;
import com.example.buycation.members.member.dto.SignupRequestDto;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.members.profile.dto.ReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public MemberResponseDto toResponse(Member member, List<ReviewResponseDto> reviewList){
        int reviewCount = member.getReviewCount();
        if (member.getReviewCount()==0) reviewCount = 1;
        return MemberResponseDto.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .userScore(member.getUserScore()/reviewCount)
                .reviewCount(member.getReviewCount())
                .reviewList(reviewList)
                .build();
    }

    public LoginResponseDto toResponse(Member member){
        return LoginResponseDto.builder()
                .memberId(member.getId())
                .build();
    }
}
