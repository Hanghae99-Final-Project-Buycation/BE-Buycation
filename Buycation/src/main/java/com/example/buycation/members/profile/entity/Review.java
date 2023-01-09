package com.example.buycation.members.profile.entity;

import com.example.buycation.common.TimeStamped;
import com.example.buycation.members.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@NoArgsConstructor
public class Review extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int reviewScore;

    @Column(nullable = false)
    private Long postingId;

    @Column(nullable = false)
    private Long reviewerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId",nullable = false)
    private Member member;

    @Builder
    public Review(int reviewScore, Long postingId, Long reviewerId, Member member) {
        this.reviewScore = reviewScore;
        this.postingId = postingId;
        this.reviewerId = reviewerId;
        this.member = member;
    }
}
