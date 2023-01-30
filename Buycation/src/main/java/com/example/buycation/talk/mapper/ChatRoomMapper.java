package com.example.buycation.talk.mapper;

import com.example.buycation.alarm.mapper.AlarmMapper;
import com.example.buycation.talk.dto.ChatRoomResponseDto;
import com.example.buycation.talk.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ChatRoomMapper {
    private final AlarmMapper alarmMapper;
    public ChatRoomResponseDto toTalkRoomResponseDto(ChatRoom talkRoom){

        int lastIndex = talkRoom.getTalks().size()>0?talkRoom.getTalks().size()-1:0;

        return ChatRoomResponseDto.builder()
                .id(talkRoom.getId())
                .postingId(talkRoom.getPosting().getId())
                .title(talkRoom.getPosting().getTitle())
                .image(talkRoom.getPosting().getImage())
                .currentMembers(talkRoom.getPosting().getCurrentMembers())
                .totalMembers(talkRoom.getPosting().getTotalMembers())
                .lastMessage(lastIndex > 0 ? talkRoom.getTalks().get(lastIndex).getMessage() : "대화 내용이 없습니다.")
                .lastReceiveTime(lastIndex > 0 ? alarmMapper.timeGapFromNow(talkRoom.getTalks().get(lastIndex).getCreatedAt()) : "")
                .dueDate(talkRoom.getPosting().getDueDate())
                .build();
    }


}
