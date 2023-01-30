package com.example.buycation.talk.mapper;

import com.example.buycation.members.member.entity.Member;
import com.example.buycation.talk.dto.TalkRequestDto;
import com.example.buycation.talk.dto.TalkResponseDto;
import com.example.buycation.talk.entity.ChatRoom;
import com.example.buycation.talk.entity.Talk;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TalkMapper {
    public TalkResponseDto toTalkResponseDto(Talk talk){
        return TalkResponseDto.builder()
                .talkRoomId(talk.getId())
                .sender(talk.getMember().getNickname())
                .memberId(talk.getMember().getId())
                .message(talk.getMessage())
                .sendDate(talk.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }

    public Talk toTalk(TalkRequestDto talkRequestDto, ChatRoom chatRoom, Member member){
        return Talk.builder()
                .member(member)
                .message(talkRequestDto.getMessage())
                .chatRoom(chatRoom)
                .build();
    }
}
