package com.example.buycation.talk.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TalkRequestDto {

    private Long roomId;
    private String sender;
    private String message;

    public TalkRequestDto(Long roomId, String sender, String message){
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
    }
}