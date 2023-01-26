package com.example.buycation.alarm.controller;


import com.example.buycation.alarm.dto.AlarmResponseDto;
import com.example.buycation.alarm.service.AlarmService;
import com.example.buycation.common.PageConfig.PageRequest;
import com.example.buycation.common.PageConfig.PageResponse;
import com.example.buycation.common.ResponseMessage;
import com.example.buycation.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;

import static com.example.buycation.common.MessageCode.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/alarm")
public class AlarmController {

    public final AlarmService alarmService;

    //memberId는 추후에 수정 필수
    @GetMapping(value = "/subscribe", produces= MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @RequestParam(required = false, defaultValue = "")String lastEventId) throws IOException{

        System.out.println("lastEventId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> SSE 연결 " + lastEventId);
        return alarmService.subscribe(userDetails, lastEventId);

    }


    @GetMapping("")
    public ResponseMessage<PageResponse<AlarmResponseDto>> getAlarmsPaging(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                   @RequestParam(value = "key" , required = false) Long key){

        PageResponse<AlarmResponseDto> alarms = alarmService.getAlarmsPaging(userDetails, new PageRequest(key));
        return new ResponseMessage<PageResponse<AlarmResponseDto>>(ALARM_SEARCH_SUCCESS, alarms);
    }

    @GetMapping("/count")
    public ResponseMessage<Long> getAlarmCount(@AuthenticationPrincipal UserDetailsImpl userDetails){
        Long count = alarmService.getAlarmCount(userDetails);
        return new ResponseMessage<Long>(ALARM_COUNT_SUCCESS, count);
    }

    @PatchMapping("/{alarmId}")
    public ResponseMessage<String> readAlarm(@PathVariable Long alarmId){
        return new ResponseMessage<>(ALARM_READ_SUCCESS, alarmService.readAlarm(alarmId));
    }

    @DeleteMapping("/{alarmId}")
    public ResponseMessage<String> deleteAlarm(@PathVariable Long alarmId){
        alarmService.deleteAlarm(alarmId);
        return new ResponseMessage<>(ALARM_DELETE_SUCCESS, null);
    }

    @DeleteMapping("")
    public ResponseMessage<?> deleteAlarm(@AuthenticationPrincipal UserDetailsImpl userDetails){
        alarmService.deleteAllAlarms(userDetails);
        return new ResponseMessage<>(ALARM_DELETE_SUCCESS, null);
    }

}
