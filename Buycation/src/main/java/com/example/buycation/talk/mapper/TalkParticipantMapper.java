package com.example.buycation.talk.mapper;

import com.example.buycation.participant.entity.Participant;
import com.example.buycation.talk.dto.ParticipantResponseDto;
import org.springframework.stereotype.Component;

@Component
public class TalkParticipantMapper {
    public ParticipantResponseDto toParticipantResponseDto(Participant participant){
        return ParticipantResponseDto.builder()
                .memberId(participant.getMember().getId())
                .nickname(participant.getMember().getNickname())
                .build();
    }
}
