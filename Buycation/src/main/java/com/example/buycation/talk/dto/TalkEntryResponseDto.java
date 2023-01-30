package com.example.buycation.talk.dto;

import com.example.buycation.participant.entity.Participant;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TalkEntryResponseDto {
    private String nickname;
    private Long memberId;
    private ChatRoomResponseDto roomInfo;
    private List<ParticipantResponseDto> participants;
    private List<TalkResponseDto> talks;
}
