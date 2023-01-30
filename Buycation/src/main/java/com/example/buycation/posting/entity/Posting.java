package com.example.buycation.posting.entity;

import com.example.buycation.comment.entity.Comment;
import com.example.buycation.common.TimeStamped;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.participant.entity.Application;
import com.example.buycation.participant.entity.Participant;
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

    @Column
    private String addressDetail;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private int totalMembers;

    @Column(nullable = false)
    private int currentMembers;

    @Column(nullable = false)
    private String dueDate;

    @Column(nullable = false)
    private long budget;

    @Column(nullable = false)
    private long perBudget;

    @Column
    private String image;

    @Column(nullable = false, length = 10000)
    private String content;

    @Column(nullable = false)
    private boolean doneStatus;

    @Column(nullable = false)
    private double coordsX;

    @Column(nullable = false)
    private double coordsY;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "posting", fetch = FetchType.LAZY)
    private List<Participant> participantList = new ArrayList<>();

    @OneToMany(mappedBy = "posting", fetch = FetchType.LAZY)
    private List<Application> applicationList = new ArrayList<>();

    @OneToMany(mappedBy = "posting", fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();

    public void add(Participant participant) {
        this.participantList.add(participant);
    }

    public void add(Application application) {
        this.applicationList.add(application);
    }

    public void add(Comment comment) {
        this.commentList.add(comment);
    }

    @Builder
    public Posting(String title, String address, String addressDetail, String category, int totalMembers, String dueDate, long budget, long perBudget,
                   String image, String content, Member member, boolean doneStatus, int currentMembers, double coordsX, double coordsY) {
        this.title = title;
        this.address = address;
        this.addressDetail = addressDetail;
        this.category = category;
        this.totalMembers = totalMembers;
        this.dueDate = dueDate;
        this.budget = budget;
        this.perBudget = perBudget;
        this.image = image;
        this.content = content;
        this.member = member;
        this.doneStatus = doneStatus;
        this.currentMembers = currentMembers;
        this.coordsX = coordsX;
        this.coordsY = coordsY;
    }

    public void finish(boolean doneStatus) {
        this.doneStatus = doneStatus;
    }

    public void addMembers(int currentMembers) {
        this.currentMembers += currentMembers;
    }

    public void update(String title, String address, String addressDetail, String category, int totalMembers,
                       String dueDate, long budget, String image, String content, double coordsX, double coordsY) {
        this.title = title;
        this.address = address;
        this.addressDetail = addressDetail;
        this.category = category;
        this.totalMembers = totalMembers;
        this.dueDate = dueDate;
        this.budget = budget;
        this.image = image;
        this.content = content;
        this.coordsX = coordsX;
        this.coordsY = coordsY;
    }
}
