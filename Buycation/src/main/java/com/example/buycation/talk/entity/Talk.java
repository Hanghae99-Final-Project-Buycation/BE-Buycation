package com.example.buycation.talk.entity;

import com.example.buycation.common.TimeStamped;
import com.example.buycation.members.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Talk extends TimeStamped {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String message;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "talk_room_id")
    private ChatRoom chatRoom;

    @Builder
    public Talk(Member member, String message, ChatRoom chatRoom) {
        this.member = member;
        this.message = message;
        this.chatRoom = chatRoom;
    }
}