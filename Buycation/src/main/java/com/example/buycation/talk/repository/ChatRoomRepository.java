package com.example.buycation.talk.repository;

import com.example.buycation.posting.entity.Posting;
import com.example.buycation.talk.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findAllByPostingIn(List<Posting> postings);

    Optional<ChatRoom> findByPosting(Posting posting);

    @Modifying
    @Query("delete from ChatRoom p where p in :chatRooms")
    void deleteAllByIdInQuery(@Param("chatRooms") List<ChatRoom> chatRooms);
}
