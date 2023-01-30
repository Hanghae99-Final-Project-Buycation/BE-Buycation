package com.example.buycation.talk.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomResponseDto {
    private Long id;
    private Long postingId;
    private String title;
    private String image;
    private String dueDate;
    private int currentMembers;
    private int totalMembers;
    private String lastMessage;
    private String lastReceiveTime;
}
