package com.example.buycation.participant.repository;

import com.example.buycation.members.member.entity.Member;
import com.example.buycation.participant.entity.Participant;
import com.example.buycation.posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Participant findByPostingAndMember(Posting posting, Member member);
}
