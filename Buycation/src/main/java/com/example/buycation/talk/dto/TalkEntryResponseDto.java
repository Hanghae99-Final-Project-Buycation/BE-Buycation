package com.example.buycation.talk.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TalkEntryResponseDto {
    private String nickname;
    private List<TalkResponseDto> talks;
}
