package com.example.buycation.talk.mapper;

import com.example.buycation.talk.dto.ChatRoomResponseDto;
import com.example.buycation.talk.entity.ChatRoom;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class ChatRoomMapper {
    public ChatRoomResponseDto toTalkRoomResponseDto(ChatRoom talkRoom){
        int talkSize = talkRoom.getTalks().size()>0?talkRoom.getTalks().size():0;
        return ChatRoomResponseDto.builder()
                .id(talkRoom.getId())
                .postingId(talkRoom.getPosting().getId())
                .title(talkRoom.getPosting().getTitle())
                .image(talkRoom.getPosting().getImage())
                .currentMembers(talkRoom.getPosting().getCurrentMembers())
                .totalMembers(talkRoom.getPosting().getTotalMembers())
                .lastMessage(talkSize>0 ? talkRoom.getTalks().get(talkSize).getMessage() : "대화 내용이 없습니다.")
                .lastReceiveTime(talkSize>0 ? talkRoom.getTalks().get(talkSize).getCreatedAt()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "")
                .dueDate(talkRoom.getPosting().getDueDate())
                .build();
    }
}
