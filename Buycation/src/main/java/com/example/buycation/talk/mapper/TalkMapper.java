package com.example.buycation.talk.mapper;

import com.example.buycation.talk.dto.TalkRequestDto;
import com.example.buycation.talk.dto.TalkResponseDto;
import com.example.buycation.talk.entity.ChatRoom;
import com.example.buycation.talk.entity.Talk;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TalkMapper {
    public TalkResponseDto toTalkResponseDto(Talk talk){
        return TalkResponseDto.builder()
                .talkRoomId(talk.getId())
                .sender(talk.getSender())
                .message(talk.getMessage())
                .sendDate(talk.getCreatedAt())
                .build();
    }

    public Talk toTalk(TalkRequestDto talkRequestDto, ChatRoom chatRoom){
        return Talk.builder()
                .sender(talkRequestDto.getSender())
                .message(talkRequestDto.getMessage())
                .chatRoom(chatRoom)
                .build();
    }
}
