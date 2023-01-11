package com.example.buycation.alarm.dto;

import com.example.buycation.alarm.entity.AlarmType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlarmResponseDto {
    private String message;
    private AlarmType type;
    private Long postingId;
    private String createdAt;
}

