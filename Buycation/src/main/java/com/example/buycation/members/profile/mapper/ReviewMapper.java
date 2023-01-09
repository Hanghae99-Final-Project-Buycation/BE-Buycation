package com.example.buycation.members.profile.mapper;

import com.example.buycation.members.member.entity.Member;
import com.example.buycation.members.profile.dto.ReviewRequestDto;
import com.example.buycation.members.profile.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toReview(ReviewRequestDto reviewRequestDto, Long reviewerId, Long posetingId, Member member){
        return Review.builder()
                .reviewScore(reviewRequestDto.getUserScore())
                .postingId(posetingId)
                .reviewerId(reviewerId)
                .member(member)
                .build();
    }
}
