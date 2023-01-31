package com.example.buycation.talk.repository;

import com.example.buycation.talk.entity.ChatRoom;
import com.example.buycation.talk.entity.Talk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TalkRepository extends JpaRepository<Talk, Long> {

    List<Talk> findAllByChatRoom(ChatRoom chatRoom);
    void deleteAllByChatRoom(ChatRoom chatRoom);
}
