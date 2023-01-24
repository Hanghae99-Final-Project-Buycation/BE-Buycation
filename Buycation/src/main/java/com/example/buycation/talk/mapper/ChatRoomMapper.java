package com.example.buycation.talk.mapper;

import com.example.buycation.talk.dto.ChatRoomResponseDto;
import com.example.buycation.talk.entity.ChatRoom;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomMapper {
    public ChatRoomResponseDto toTalkRoomResponseDto(ChatRoom talkRoom){
        return ChatRoomResponseDto.builder()
                .id(talkRoom.getId())
                .postingId(talkRoom.getPosting().getId())
                .title(talkRoom.getPosting().getTitle())
                .currentMembers(talkRoom.getPosting().getCurrentMembers())
                .dueDate(talkRoom.getPosting().getDueDate())
                .build();
    }
}
