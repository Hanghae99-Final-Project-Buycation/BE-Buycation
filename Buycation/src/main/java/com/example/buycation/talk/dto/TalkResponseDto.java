package com.example.buycation.talk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
public class TalkResponseDto {

    private Long talkRoomId;
    private Long memberId;
    private String sender;
    private String message;

    private LocalDateTime sendDate;

}