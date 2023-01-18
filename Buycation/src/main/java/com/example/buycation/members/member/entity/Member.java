package com.example.buycation.members.member.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private int userScore;

    @Column(nullable = false)
    private int reviewCount;

    @Column(nullable = false)
    private String profileImage;

    @Column(nullable = false)
    private String address;

    @Column
    private Long kakaoId;

    @Builder
    public Member(String email, String password, String nickname, String profileImage, String address) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.address = address;
    }

    public Member(String email, String password, String nickname, String profileImage, String address, Long kakaoId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.address = address;
        this.kakaoId = kakaoId;
    }

    public void addScore(int userScore, int reviewCount) {
        this.userScore += userScore;
        this.reviewCount += reviewCount;
    }

    public void update(String nickname, String profileImage, String address) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.address = address;
    }

    public Member kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }
}

