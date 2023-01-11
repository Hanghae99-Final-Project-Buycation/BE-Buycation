package com.example.buycation.alarm.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlarmCountDto {
    private Long count;

    public AlarmCountDto(Long count){
        this.count = count;
    }
}
