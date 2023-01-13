package com.example.buycation.members.profile.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponseDto {
    private int reviewScore;
    private String createAt;
}
