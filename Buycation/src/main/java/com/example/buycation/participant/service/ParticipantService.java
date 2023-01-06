package com.example.buycation.participant.service;

import com.example.buycation.common.exception.CustomException;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.participant.dto.ApplicationResponseDto;
import com.example.buycation.participant.entity.Application;
import com.example.buycation.participant.entity.Participant;
import com.example.buycation.participant.mapper.ApplicationMapper;
import com.example.buycation.participant.repository.ApplicationRepository;
import com.example.buycation.participant.repository.ParticipantRepository;
import com.example.buycation.posting.entity.Posting;
import com.example.buycation.posting.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.buycation.common.exception.ErrorCode.APPLICANT_NOT_FOUND;
import static com.example.buycation.common.exception.ErrorCode.AUTHORIZATION_APPLICANT_LOOKUP_FAIL;
import static com.example.buycation.common.exception.ErrorCode.AUTHORIZATION_DECISION_FAIL;
import static com.example.buycation.common.exception.ErrorCode.DUPLICATE_APPLICATION;
import static com.example.buycation.common.exception.ErrorCode.DUPLICATE_PARTICIPATION;
import static com.example.buycation.common.exception.ErrorCode.PARTICIPANT_NOT_FOUND;
import static com.example.buycation.common.exception.ErrorCode.POSTING_NOT_FOUND;
import static com.example.buycation.common.exception.ErrorCode.WRITER_PARTICIPATION_CANAEL;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ApplicationRepository applicationRepository;
    private final PostingRepository postingRepository;
    private final ApplicationMapper applicationMapper;
    private final ParticipantRepository participantRepository;

    @Transactional
    public void createApplicant(Member member, Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));

        //작성자 지원신청 금지
        if (posting.getMember().getId().equals(member.getId())){
            throw new CustomException(DUPLICATE_PARTICIPATION);
        }
        //지원자 중복 지원 금지
        if (applicationRepository.findByPostingAndMember(posting, member) != null){
            throw new CustomException(DUPLICATE_APPLICATION);
        }
        //지원자 참가수락완료 후 지원 금지
        if (participantRepository.findByPostingAndMember(posting, member) != null){
            throw new CustomException(DUPLICATE_PARTICIPATION);
        }

        Application application = applicationMapper.toApplication(member,posting);
        applicationRepository.save(application);
        posting.add(application);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponseDto> getApplicant(Member member, Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));

        //작성자만 조회 권한
        if (!posting.getMember().getId().equals(member.getId())){
            throw new CustomException(AUTHORIZATION_APPLICANT_LOOKUP_FAIL);
        }

        List<Application> applications = applicationRepository.findAllByPosting(posting);
        List<ApplicationResponseDto> applicationList = new ArrayList<>();
        for (Application a : applications) {
            applicationList.add(applicationMapper.toResponse(a));
        }
        return applicationList;
    }

    @Transactional
    public void applicantAccept(Member member, Long postingId, Long applicationId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));

        //작성자만 결정 권한
        if (!posting.getMember().getId().equals(member.getId())){
            throw new CustomException(AUTHORIZATION_DECISION_FAIL);
        }

        Application application = applicationRepository.findById(applicationId).orElseThrow(()->new CustomException(APPLICANT_NOT_FOUND));

        //지원 수락된 참가자 중복수락 금지
        if (participantRepository.findByPostingAndMember(application.getPosting(), application.getMember()) != null){
            throw new CustomException(DUPLICATE_PARTICIPATION);
        }

        Participant participant = applicationMapper.toParticipant(application);
        participantRepository.save(participant);
        posting.add(participant);

        applicationRepository.deleteById(applicationId);
    }

    @Transactional
    public void applicantRefuse(Member member, Long postingId, Long applicationId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));

        //작성자만 결정 권한
        if (!posting.getMember().getId().equals(member.getId())){
            throw new CustomException(AUTHORIZATION_DECISION_FAIL);
        }

        Application application = applicationRepository.findById(applicationId).orElseThrow(()->new CustomException(APPLICANT_NOT_FOUND));

        //지원 수락된 참가자 중복거절 금지
        if (participantRepository.findByPostingAndMember(application.getPosting(), application.getMember()) != null){
            throw new CustomException(DUPLICATE_PARTICIPATION);
        }

        applicationRepository.deleteById(applicationId);

    }

    @Transactional
    public void cancelParticipation(Member member, Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));

        //게시글 작성자 참가취소 금지
        if (posting.getMember().getId().equals(member.getId())){
            throw new CustomException(WRITER_PARTICIPATION_CANAEL);
        }

        Participant participant = participantRepository.findByPostingAndMember(posting, member);

        if (participant == null){
            throw new CustomException(PARTICIPANT_NOT_FOUND);
        }

        participantRepository.deleteById(participant.getId());
    }
}
