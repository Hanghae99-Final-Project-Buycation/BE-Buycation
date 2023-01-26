package com.example.buycation.talk.repository;

import com.example.buycation.posting.entity.Posting;
import com.example.buycation.talk.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByPostingIn(List<Posting> postings);
}
