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
    private String message;

    @Column
    private Boolean read;

    @Enumerated
    @Column
    private AlarmType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;

    @Builder
    public Alarm(Long postingId, String message, Boolean read, AlarmType type, Member member){
        this.message = message;
        this.read = read;
        this.type = type;
        this.member = member;
        this.postingId = postingId;
    }

    public void read() {
        this.read = true;
    }
}