package com.example.buycation.members.profile.service;

import com.example.buycation.common.exception.CustomException;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.members.member.repository.MemberRepository;
import com.example.buycation.members.profile.dto.ReviewRequestDto;
import com.example.buycation.members.profile.entity.Review;
import com.example.buycation.members.profile.mapper.ReviewMapper;
import com.example.buycation.members.profile.repository.ReviewRepository;
import com.example.buycation.participant.repository.ParticipantRepository;
import com.example.buycation.posting.entity.Posting;
import com.example.buycation.posting.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.buycation.common.exception.ErrorCode.DUPLICATE_REVIEW;
import static com.example.buycation.common.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.example.buycation.common.exception.ErrorCode.POSTING_NOT_FOUND;
import static com.example.buycation.common.exception.ErrorCode.POSTING_PARTICIPANT_REVIEW;
import static com.example.buycation.common.exception.ErrorCode.POSTING_SUCCESS_ERROR;
import static com.example.buycation.common.exception.ErrorCode.SELF_REVIEW_ERROR;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final PostingRepository postingRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public void createReview(ReviewRequestDto reviewRequestDto, Member reviewer, Long postingId, Long memberId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        //게시글 완료 전 리뷰 금지
        if (!posting.isDoneStatus()){
            throw new CustomException(POSTING_SUCCESS_ERROR);
        }
        //셀프리뷰금지
        if (memberId.equals(reviewer.getId())) {
            throw new CustomException(SELF_REVIEW_ERROR);
        }
        //참가자외에 리뷰 금지
        if (participantRepository.findByPostingAndMember(posting, reviewer) == null) {
            throw new CustomException(POSTING_PARTICIPANT_REVIEW);
        }
        //중복리뷰 금지
        if (reviewRepository.findByPostingIdAndReviewerIdAndMember(postingId, reviewer.getId(), member) != null) {
            throw new CustomException(DUPLICATE_REVIEW);
        }

        Review review = reviewMapper.toReview(reviewRequestDto, reviewer.getId(), postingId, member);
        reviewRepository.save(review);
        member.addScore(review.getReviewScore(), 1);
    }
}
