package com.example.buycation.participant.mapper;

import com.example.buycation.members.member.entity.Member;
import com.example.buycation.participant.dto.ApplicationResponseDto;
import com.example.buycation.participant.entity.Application;
import com.example.buycation.participant.entity.Participant;
import com.example.buycation.posting.entity.Posting;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapper {

    public ApplicationResponseDto toResponse(Application application) {
        int reviewCount = application.getMember().getReviewCount();
        /// by zero 예외 방지
        if (application.getMember().getReviewCount()==0) reviewCount = 1;
        return ApplicationResponseDto.builder()
                .applicationId(application.getId())
                .memberId(application.getMember().getId())
                .nickname(application.getMember().getNickname())
                .profileImage(application.getMember().getProfileImage())
                .userScore(application.getMember().getUserScore()/reviewCount)
                .build();
    }

    public Application toApplication(Member member, Posting posting) {
        return Application.builder()
                .member(member)
                .posting(posting)
                .build();
    }

    public Participant toParticipant(Application application) {
        return Participant.builder()
                .member(application.getMember())
                .posting(application.getPosting())
                .build();
    }
}
