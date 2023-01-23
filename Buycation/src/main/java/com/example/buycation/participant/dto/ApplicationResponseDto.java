package com.example.buycation.participant.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationResponseDto {
    private Long applicationId;
    private Long memberId;
    private String nickname;
    private String profileImage;
    private int userScore;
}
