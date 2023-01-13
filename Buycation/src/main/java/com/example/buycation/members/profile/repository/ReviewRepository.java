package com.example.buycation.members.profile.repository;

import com.example.buycation.members.member.entity.Member;
import com.example.buycation.members.profile.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByPostingIdAndReviewerIdAndMember(Long postingId, Long id, Member member);

    List<Review> findAllByMemberIdOrderByCreatedAtDesc(Long memberId);
}
