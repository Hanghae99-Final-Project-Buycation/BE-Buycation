package com.example.buycation.talk.service;


import com.example.buycation.common.exception.CustomException;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.participant.repository.ParticipantRepository;
import com.example.buycation.posting.entity.Posting;
import com.example.buycation.security.UserDetailsImpl;
import com.example.buycation.talk.dto.TalkEntryResponseDto;
import com.example.buycation.talk.dto.TalkRequestDto;
import com.example.buycation.talk.dto.TalkResponseDto;
import com.example.buycation.talk.dto.ChatRoomResponseDto;
import com.example.buycation.talk.entity.Talk;
import com.example.buycation.talk.entity.ChatRoom;
import com.example.buycation.talk.mapper.ChatRoomMapper;
import com.example.buycation.talk.mapper.TalkMapper;
import com.example.buycation.talk.repository.TalkRepository;
import com.example.buycation.talk.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.buycation.common.exception.ErrorCode.AUTHORIZATION_TALKROOM;
import static com.example.buycation.common.exception.ErrorCode.TALKROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TalkService {
    private final ChatRoomRepository chatRoomRepository;
    private final TalkRepository talkRepository;
    private final ParticipantRepository participantRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final TalkMapper talkMapper;

    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> findAllRoom(UserDetailsImpl userDetails){
        Member member = userDetails.getMember();
        List<Posting> postings = participantRepository.findAllByMember(member).stream().map(participant -> participant.getPosting()).toList();
        List<ChatRoom> talkRooms = chatRoomRepository.findAllByPostingIn(postings);
        return talkRooms.stream().map(chatRoomMapper::toTalkRoomResponseDto).toList();
    }

    @Transactional
    public void createRoom(Posting posting){
        chatRoomRepository.save(new ChatRoom(posting));
    }


    @Transactional
    public TalkEntryResponseDto findAllMessageByTalkRoomId(Long roomId, UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                ()-> new CustomException(TALKROOM_NOT_FOUND)
        );

        Long checkPaticipantInChatRoom = chatRoom.getPosting().getParticipantList()
                .stream()
                .filter(participant -> participant.getMember().getId().equals(member.getId()))
                .count();

        if(checkPaticipantInChatRoom.equals(0)){
            throw new CustomException(AUTHORIZATION_TALKROOM);
        }

        List<Talk> talks = talkRepository.findAllByChatRoom(chatRoom);
        List<TalkResponseDto> talksDto = talks.stream().map(talk -> talkMapper.toTalkResponseDto(talk)).toList();

        return TalkEntryResponseDto.builder()
                .nickname(member.getNickname())
                .talks(talksDto)
                .build();
    }


    public TalkResponseDto createMessage(Long roomId, TalkRequestDto talkRequestDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow();
        Talk talk = talkMapper.toTalk(talkRequestDto, chatRoom);
        talkRepository.save(talk);
        return talkMapper.toTalkResponseDto(talk);
    }
}
