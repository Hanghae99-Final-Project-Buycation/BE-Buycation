package com.example.buycation.participant.service;

import com.example.buycation.common.exception.CustomException;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.participant.dto.ApplicationResponseDto;
import com.example.buycation.participant.entity.Application;
import com.example.buycation.participant.mapper.ApplicationMapper;
import com.example.buycation.participant.repository.ApplicationRepository;
import com.example.buycation.posting.entity.Posting;
import com.example.buycation.posting.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.buycation.common.exception.ErrorCode.AUTHORIZATION_APPLICANT_LOOKUP_FAIL;
import static com.example.buycation.common.exception.ErrorCode.DUPLICATE_MEMBER;
import static com.example.buycation.common.exception.ErrorCode.POSTING_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ApplicationRepository applicationRepository;
    private final PostingRepository postingRepository;
    private final ApplicationMapper applicationMapper;

    @Transactional
    public void createApplicant(Member member, Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));

        if (applicationRepository.findByPostingAndMember(posting, member) != null){
            throw new CustomException(DUPLICATE_MEMBER);
        }

        Application application = applicationMapper.toApplication(member,posting);
        applicationRepository.save(application);
        posting.add(application);
    }

    @Transactional(readOnly = true)
    public List<ApplicationResponseDto> getApplicant(Member member, Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));

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
}
