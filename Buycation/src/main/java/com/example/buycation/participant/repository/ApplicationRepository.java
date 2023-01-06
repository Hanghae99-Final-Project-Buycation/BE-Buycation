package com.example.buycation.participant.repository;

import com.example.buycation.members.member.entity.Member;
import com.example.buycation.participant.entity.Application;
import com.example.buycation.posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findAllByPosting(Posting posting);
    Application findByPostingAndMember(Posting posting, Member member);
}
