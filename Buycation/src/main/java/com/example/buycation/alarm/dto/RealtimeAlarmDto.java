package com.example.buycation.alarm.dto;


import com.example.buycation.alarm.entity.AlarmType;
import com.example.buycation.members.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RealtimeAlarmDto {
    private String message;
    private AlarmType type;
    private Member member;
    private AlarmType alarmType;
    private Long postingId;
    private String title;
}
