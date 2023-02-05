package com.example.buycation.talk.mapper;

import com.example.buycation.members.member.entity.Member;
import com.example.buycation.talk.dto.TalkRedisDto;
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
        LocalDateTime sendDate = talk.getCreatedAt() == null?LocalDateTime.now():talk.getCreatedAt();
        return TalkResponseDto.builder()
                .id(talk.getId())
                .talkRoomId(talk.getChatRoom().getId())
                .sender(talk.getMember().getNickname())
                .memberId(talk.getMember().getId())
                .message(talk.getMessage())
                .sendDate(sendDate.format(DateTimeFormatter.ofPattern("MM월 dd일 HH:mm")))
                .build();
    }

    public TalkResponseDto dtoToTalkResponseDto(TalkRedisDto talkRedisDto){;
        return TalkResponseDto.builder()
                .id(talkRedisDto.getId())
                .talkRoomId(talkRedisDto.getTalkRoomId())
                .sender(talkRedisDto.getSender())
                .memberId(talkRedisDto.getMemberId())
                .message(talkRedisDto.getMessage())
                .sendDate(talkRedisDto.getSendDate().format(DateTimeFormatter.ofPattern("MM월 dd일 HH:mm")))
                .build();
    }

    public TalkRedisDto dtoToTalkRedisDto(TalkRequestDto talkRequestDto) {
        return TalkRedisDto.builder()
                .talkRoomId(talkRequestDto.getRoomId())
                .message(talkRequestDto.getMessage())
                .sender(talkRequestDto.getSender())
                .memberId(talkRequestDto.getMemberId())
                .sendDate(LocalDateTime.now()).build();
    }
}
