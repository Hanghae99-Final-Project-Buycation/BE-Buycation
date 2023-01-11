package com.example.buycation.alarm.controller;


import com.example.buycation.alarm.dto.AlarmResponseDto;
import com.example.buycation.alarm.service.AlarmService;
import com.example.buycation.common.ResponseMessage;
import com.example.buycation.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

import static com.example.buycation.common.MessageCode.*;


@RequiredArgsConstructor
@Controller
public class AlarmController {

    public final AlarmService alarmService;

    //memberId는 추후에 수정 필수
    @GetMapping(value = "/alarm/subs/{memberId}", produces= MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @RequestParam(required = false, defaultValue = "")String lastEventId,
                                @PathVariable Long memberId) throws IOException{

        System.out.println("lastEventId :::: " + lastEventId);
        return alarmService.subscribe(memberId, lastEventId);

    }



    @GetMapping("/alarm")
    public ResponseMessage<List<AlarmResponseDto>> getAlarms(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<AlarmResponseDto> alarms = alarmService.getAlarms(userDetails);
        return new ResponseMessage<List<AlarmResponseDto>>(ALARM_SEARCH_SUCCESS, alarms);
    }

    @GetMapping("/alarm/count")
    public ResponseMessage<Long> getAlarmCount(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long count = alarmService.getAlarmCount(1L);
        return new ResponseMessage<Long>(ALARM_COUNT_SUCCESS, count);
    }

    @PatchMapping("/alarm/{alarmId}")
    public ResponseMessage<String> readAlarm(@PathVariable Long alarmId){
        return new ResponseMessage<>(ALARM_READ_SUCCESS, alarmService.readAlarm(alarmId));
    }

    @DeleteMapping("/alarm/{alarmId}")
    public ResponseMessage<String> deleteAlarm(@PathVariable Long alarmId){
        alarmService.deleteAlarm(alarmId);
        return new ResponseMessage<>(ALARM_DELETE_SUCCESS, null);
    }

    @DeleteMapping("/alarm")
    public ResponseMessage<?> deleteAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails){
        alarmService.deleteAllAlarms(userDetails);
        return new ResponseMessage<>(ALARM_DELETE_SUCCESS, null);
    }



}
