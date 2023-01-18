package com.example.buycation.alarm.dto;


import com.example.buycation.alarm.entity.AlarmType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RealtimeAlarmDto {
    private String message;
    private AlarmType type;
    private Long postingId;
}
