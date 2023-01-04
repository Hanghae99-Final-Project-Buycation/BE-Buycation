package com.example.buycation.posting.entity;

import com.example.buycation.common.TimeStamped;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.participant.entity.Application;
import com.example.buycation.participant.entity.Participant;
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
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Posting extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private int totalMembers = 0;

    @Column(nullable = false)
    private int minMembers;

    @Column(nullable = false)
    private String dueDate;

    @Column
    private long budget;

    @Column
    private String image;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean status = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId",nullable = false)
    private Member member;

    @OneToMany(mappedBy = "posting",fetch = FetchType.LAZY)
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "posting",fetch = FetchType.LAZY)
    private List<Application> applications = new ArrayList<>();
}
