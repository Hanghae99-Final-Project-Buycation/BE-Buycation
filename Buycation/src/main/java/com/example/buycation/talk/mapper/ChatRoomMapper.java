package com.example.buycation.talk.mapper;

import com.example.buycation.talk.dto.ChatRoomResponseDto;
import com.example.buycation.talk.entity.ChatRoom;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class ChatRoomMapper {
    public ChatRoomResponseDto toTalkRoomResponseDto(ChatRoom talkRoom){
        return ChatRoomResponseDto.builder()
                .id(talkRoom.getId())
                .postingId(talkRoom.getPosting().getId())
                .title(talkRoom.getPosting().getTitle())
                .image(talkRoom.getPosting().getImage())
                .currentMembers(talkRoom.getPosting().getCurrentMembers())
                .totalMembers(talkRoom.getPosting().getTotalMembers())
                .lastMessage(talkRoom.getTalks().get(talkRoom.getTalks().size() - 1).getMessage())
                .lastReceiveTime(talkRoom.getTalks().get(talkRoom.getTalks().size() - 1).getCreatedAt()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .dueDate(talkRoom.getPosting().getDueDate())
                .build();
    }
}
