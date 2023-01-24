package com.example.buycation.alarm.dto;

import com.example.buycation.alarm.entity.AlarmType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlarmResponseDto {
    private Long alarmId;
    private AlarmType type;
    private boolean read;
    private Long postingId;
    private String title;
    private String message;
    private String createdAt;
}

