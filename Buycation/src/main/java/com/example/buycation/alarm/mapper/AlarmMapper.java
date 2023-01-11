package com.example.buycation.alarm.mapper;

import com.example.buycation.alarm.dto.AlarmResponseDto;
import com.example.buycation.alarm.entity.Alarm;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class AlarmMapper {
    public AlarmResponseDto toAlarmResponseDto(Alarm alarm){
        return AlarmResponseDto.builder()
                .postingId(alarm.getPostingId())
                .message(alarm.getMessage())
                .type(alarm.getType())
                .createdAt(alarm.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();

    }

}
