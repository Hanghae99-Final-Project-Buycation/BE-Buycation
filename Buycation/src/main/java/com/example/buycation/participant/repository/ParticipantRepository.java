package com.example.buycation.participant.repository;

import com.example.buycation.members.member.entity.Member;
import com.example.buycation.participant.entity.Participant;
import com.example.buycation.posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Participant findByPostingAndMember(Posting posting, Member member);

    List<Participant> findAllByPosting(Posting posting);

    @Modifying
    @Query("delete from Participant p where p in :participants")
    void deleteAllByInQuery(@Param("participants") List<Participant> participants);

    List<Participant> findAllByMember(Member member);
}
