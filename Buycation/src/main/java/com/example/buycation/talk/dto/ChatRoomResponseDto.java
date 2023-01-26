package com.example.buycation.talk.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponseDto {
    private Long id;
    private Long postingId;
    private String title;
    private String dueDate;
    private int currentMembers;

}
