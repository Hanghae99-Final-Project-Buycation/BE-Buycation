package com.example.buycation.alarm.controller;


import com.example.buycation.alarm.service.AlarmService;
import com.example.buycation.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class AlarmController {

    public final AlarmService alarmService;

    @GetMapping(value = "/alarm/subs", produces= MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return alarmService.subscribe(1L);
    }





}
