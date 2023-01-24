package com.example.buycation.alarm.mapper;

import com.example.buycation.alarm.dto.AlarmResponseDto;
import com.example.buycation.alarm.dto.RealtimeAlarmDto;
import com.example.buycation.alarm.entity.Alarm;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AlarmMapper {
    public AlarmResponseDto toAlarmResponseDto(Alarm alarm){
        return AlarmResponseDto.builder()
                .alarmId(alarm.getId())
                .postingId(alarm.getPostingId())
                .message(alarm.getMessage())
                .read(alarm.getIsRead())
                .type(alarm.getType())
                .createdAt(timeGapFromNow(alarm.getCreatedAt()))
                .build();

    }

    public RealtimeAlarmDto toRealtimeAlarmDto(Alarm alarm){
        return RealtimeAlarmDto.builder()
                .postingId(alarm.getPostingId())
                .message(alarm.getMessage())
                .type(alarm.getType())
                .build();

    }

    private String timeGapFromNow(LocalDateTime time){
        Duration duration = Duration.between(time, LocalDateTime.now());
        long sec = duration.getSeconds();
        String timeGap = "";
        if(sec/(60*60*24) >= 1)    timeGap = (sec/(60*60*24)) + "일 전";
        else if(sec/(60*60) >= 1 ) timeGap =(sec/(60*60)) + "시간 전";
        else if(sec/60 >= 1)       timeGap =(sec/(60)) + "분 전";
        else                       timeGap =(sec + "초 전");
        return timeGap;
    }

}
