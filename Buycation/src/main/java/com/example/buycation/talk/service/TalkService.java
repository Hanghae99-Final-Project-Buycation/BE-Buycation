package com.example.buycation.talk.service;


import com.example.buycation.common.PageConfig.PageRequest;
import com.example.buycation.common.exception.CustomException;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.members.member.repository.MemberRepository;
import com.example.buycation.participant.entity.Participant;
import com.example.buycation.participant.repository.ParticipantRepository;
import com.example.buycation.posting.entity.Posting;
import com.example.buycation.security.UserDetailsImpl;
import com.example.buycation.talk.dto.*;
import com.example.buycation.talk.entity.Talk;
import com.example.buycation.talk.entity.ChatRoom;
import com.example.buycation.talk.mapper.ChatRoomMapper;
import com.example.buycation.talk.mapper.TalkParticipantMapper;
import com.example.buycation.talk.mapper.TalkMapper;
import com.example.buycation.talk.repository.TalkRedisRepository;
import com.example.buycation.talk.repository.TalkRepository;
import com.example.buycation.talk.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.example.buycation.common.exception.ErrorCode.AUTHORIZATION_TALKROOM;
import static com.example.buycation.common.exception.ErrorCode.TALKROOM_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TalkService {
    private final ChatRoomRepository chatRoomRepository;
    private final TalkRepository talkRepository;
    private final ParticipantRepository participantRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomMapper chatRoomMapper;
    private final TalkMapper talkMapper;
    private final TalkParticipantMapper participantMapper;
    private final TalkRedisRepository talkRedisRepository;

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
    public TalkEntryResponseDto findAllMessageFromRedis(Long roomId, UserDetailsImpl userDetails, PageRequest pageRequest) {

        List<TalkRedisDto> talkRedisDtos = new ArrayList<>();
        List<TalkResponseDto> talkResponseDtos = new ArrayList<>();
        List<ParticipantResponseDto> ParticipantResponseDtos = new ArrayList<>();
        List<Talk> talks = new ArrayList<>();
        Long nextKey = null;

        Member member = userDetails.getMember();

        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new CustomException(TALKROOM_NOT_FOUND)
        );

        for (Participant participant : chatRoom.getPosting().getParticipantList()) {
            ParticipantResponseDto participantResponseDto = participantMapper.toParticipantResponseDto(participant);
            ParticipantResponseDtos.add(participantResponseDto);
        }

        if (pageRequest.hasKey(pageRequest.getKey())) {

            talks = talkRepository.findTop50ByIdLessThanAndChatRoomOrderByIdDesc(pageRequest.getKey(), chatRoom);
            nextKey = talks.stream().mapToLong(Talk::getId).min().orElse(PageRequest.NONE_KEY);
        } else {

            talkRedisDtos = talkRedisRepository.findMsgByRoomId(roomId);

            if (talkRedisDtos.size() < 50) {
                talks = talkRepository.findTop50ByChatRoomOrderByIdDesc(chatRoom);
                nextKey = talks.stream().mapToLong(Talk::getId).min().orElse(PageRequest.NONE_KEY);
            }
        }

        Collections.reverse(talks);
        talkResponseDtos.addAll(talks.stream().map(talkMapper::toTalkResponseDto).toList());
        talkRedisDtos.stream().forEach(talkRedisDto -> {
            talkResponseDtos.add(talkMapper.dtoToTalkResponseDto(talkRedisDto));
        });


        return TalkEntryResponseDto.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .talks(talkResponseDtos)
                .participants(ParticipantResponseDtos)
                .roomInfo(chatRoomMapper.toTalkRoomResponseDto(chatRoom))
                .key(nextKey)
                .build();
    }


    @Transactional
    public TalkRedisDto createMessage(Long roomId, TalkRequestDto talkRequestDto) {

        Optional<Member> member= memberRepository.findById(talkRequestDto.getMemberId());
        if(member.isEmpty()){
            throw new CustomException(AUTHORIZATION_TALKROOM);
        }

        TalkRedisDto talkRedisDto = talkMapper.dtoToTalkRedisDto(talkRequestDto, roomId);
        talkRedisRepository.addMessageInRedis(roomId, talkRedisDto);
        return talkRedisDto;

    }
}
