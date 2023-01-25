package com.example.buycation.talk.entity;

import com.example.buycation.posting.entity.Posting;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Posting posting;

    public ChatRoom(Posting posting){
        this.posting = posting;
    }

}
