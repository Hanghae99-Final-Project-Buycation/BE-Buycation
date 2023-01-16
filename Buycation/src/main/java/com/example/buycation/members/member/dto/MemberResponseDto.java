package com.example.buycation.members.member.dto;

import com.example.buycation.members.profile.dto.ReviewResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberResponseDto {
    private Long memberId;
    private String email;
    private String nickname;
    private String profileImage;
    private String address;
    private int userScore;
    private int reviewCount;
    private List<ReviewResponseDto> reviewList;
}
