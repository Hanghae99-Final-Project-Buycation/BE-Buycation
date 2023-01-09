package com.example.buycation.members.profile.mapper;

import com.example.buycation.members.member.entity.Member;
import com.example.buycation.members.profile.dto.ReviewRequestDto;
import com.example.buycation.members.profile.dto.MemberReviewResponseDto;
import com.example.buycation.members.profile.entity.Review;
import com.example.buycation.participant.entity.Participant;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public MemberReviewResponseDto toResponse(Participant participant) {
        return MemberReviewResponseDto.builder()
                .postingId(participant.getPosting().getId())
                .memberId(participant.getMember().getId())
                .nickname(participant.getMember().getNickname())
                .build();
    }

    public Review toReview(ReviewRequestDto reviewRequestDto, Long reviewerId, Long posetingId, Member member) {
        return Review.builder()
                .reviewScore(reviewRequestDto.getUserScore())
                .postingId(posetingId)
                .reviewerId(reviewerId)
                .member(member)
                .build();
    }
}
