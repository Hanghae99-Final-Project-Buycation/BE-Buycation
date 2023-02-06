package com.example.buycation.talk.repository;

import com.example.buycation.talk.entity.ChatRoom;
import com.example.buycation.talk.entity.Talk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TalkRepository extends JpaRepository<Talk, Long> {
    @Modifying
    @Query("delete from Talk t where t in :talks")
    void deleteAllByInQuery(@Param("talks") List<Talk> talks);

    List<Talk> findAllByChatRoom(ChatRoom chatRoom);

    List<Talk> findTop50ByChatRoomOrderByIdDesc(ChatRoom chatRoom);
    List<Talk> findTop50ByIdLessThanAndChatRoomOrderByIdDesc(Long id, ChatRoom chatRoom);

}
