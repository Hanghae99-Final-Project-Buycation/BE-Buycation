package com.example.buycation.talk.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParticipantResponseDto {
    private Long memberId;
    private String nickname;
}
