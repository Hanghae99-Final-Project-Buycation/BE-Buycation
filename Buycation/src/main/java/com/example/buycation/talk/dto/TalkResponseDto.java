package com.example.buycation.talk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TalkResponseDto implements Serializable{
    private Long id;
    private Long talkRoomId;
    private Long memberId;
    private String sender;
    private String message;
    private String sendDate;
}