package com.example.buycation.members.profile.mapper;

import com.example.buycation.members.member.entity.Member;
import com.example.buycation.members.profile.dto.MemberReviewResponseDto;
import com.example.buycation.members.profile.dto.ReviewRequestDto;
import com.example.buycation.members.profile.dto.ReviewResponseDto;
import com.example.buycation.members.profile.entity.Review;
import com.example.buycation.participant.entity.Participant;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class ReviewMapper {

    public MemberReviewResponseDto toResponse(Participant participant, boolean status) {
        return MemberReviewResponseDto.builder()
                .postingId(participant.getPosting().getId())
                .memberId(participant.getMember().getId())
                .nickname(participant.getMember().getNickname())
                .status(status)
                .build();
    }

    public ReviewResponseDto toResponse(Review review){
        return ReviewResponseDto.builder()
                .reviewScore(review.getReviewScore())
                .createAt(review.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
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
