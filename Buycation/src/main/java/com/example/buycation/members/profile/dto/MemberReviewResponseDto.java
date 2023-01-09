package com.example.buycation.members.profile.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberReviewResponseDto {
    private Long postingId;
    private Long memberId;
    private String nickname;
}
