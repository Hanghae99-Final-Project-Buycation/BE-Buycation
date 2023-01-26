package com.example.buycation.alarm.entity;

import com.example.buycation.common.TimeStamped;
import com.example.buycation.members.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Alarm extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column
    private Long postingId;

    @Column
    private String title;

    @Column
    private String message;

    @Column
    private Boolean isRead;

    @Enumerated
    @Column
    private AlarmType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;

    @Builder
    public Alarm(Long postingId, String title, String message, Boolean isRead, AlarmType type, Member member){
        this.message = message;
        this.isRead = isRead;
        this.type = type;
        this.member = member;
        this.postingId = postingId;
        this.title = title;
    }

    public void read() {
        this.isRead = true;
    }
}