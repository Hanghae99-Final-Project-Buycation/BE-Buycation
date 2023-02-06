package com.example.buycation.talk.controller;

import com.example.buycation.talk.dto.TalkRedisDto;
import com.example.buycation.talk.dto.TalkRequestDto;
import com.example.buycation.talk.dto.TalkResponseDto;
import com.example.buycation.talk.entity.Talk;
import com.example.buycation.talk.service.TalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class TalkController {

    private final TalkService talkService;

    @MessageMapping("/{roomId}")
    @SendTo("/talk/{roomId}")
    public TalkResponseDto talk(@DestinationVariable Long roomId, TalkRequestDto talkRequestDto){
//    @PostMapping("/talk/{roomId}")
//    public TalkResponseDto talk(@PathVariable Long roomId, @RequestBody TalkRequestDto talkRequestDto){
        TalkRedisDto talkRedisDto = talkService.createMessage(roomId, talkRequestDto);
        return TalkResponseDto.builder()
                .talkRoomId(roomId)
                .memberId(talkRedisDto.getMemberId())
                .sender(talkRedisDto.getSender())
                .message(talkRedisDto.getMessage())
                .sendDate(talkRedisDto.getSendDate().format(DateTimeFormatter.ofPattern("MM월 dd일 HH:mm")))
                .build();
    }
}
