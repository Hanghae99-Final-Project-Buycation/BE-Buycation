package com.example.buycation.talk.controller;

import com.example.buycation.talk.dto.TalkRequestDto;
import com.example.buycation.talk.dto.TalkResponseDto;
import com.example.buycation.talk.entity.Talk;
import com.example.buycation.talk.service.TalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TalkController {

    private final TalkService talkService;

    @MessageMapping("/{roomId}")
    @SendTo("/talk/{roomId}")
    public TalkResponseDto talk(@DestinationVariable Long talkRoomId, TalkRequestDto talkRequestDto){
        System.out.println("talk ==> " + talkRequestDto.getMessage());
        TalkResponseDto newTalk = talkService.createMessage(talkRoomId, talkRequestDto);
        return TalkResponseDto.builder()
                .talkRoomId(talkRoomId)
                .sender(newTalk.getSender())
                .message(newTalk.getMessage())
                .build();

    }
}
