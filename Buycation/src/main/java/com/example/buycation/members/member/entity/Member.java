package com.example.buycation.members.member.entity;

import com.example.buycation.members.profile.entity.Review;
import com.example.buycation.posting.entity.Posting;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

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

    @Column
    private String profileImage;

    @Column
    private String address;

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private List<Posting> postings = new ArrayList<>();


}
