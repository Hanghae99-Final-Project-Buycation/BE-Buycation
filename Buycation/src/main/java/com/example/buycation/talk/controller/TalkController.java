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
    public TalkResponseDto talk(@DestinationVariable Long roomId, TalkRequestDto talkRequestDto){
        TalkResponseDto newTalk = talkService.createMessage(roomId, talkRequestDto);
        return TalkResponseDto.builder()
                .talkRoomId(roomId)
                .memberId(newTalk.getMemberId())
                .sender(newTalk.getSender())
                .message(newTalk.getMessage())
                .build();
    }
}
