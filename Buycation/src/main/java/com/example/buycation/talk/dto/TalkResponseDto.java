package com.example.buycation.talk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class TalkResponseDto {

    private Long talkRoomId;
    private String sender;
    private String message;

    private LocalDateTime sendDate;

    @Builder
    public TalkResponseDto(Long talkRoomId, String sender, String message, LocalDateTime sendDate){
        this.talkRoomId = talkRoomId;
        this.sender = sender;
        this.message = message;
        this.sendDate = sendDate;
    }
}