package com.example.buycation.participant.repository;

import com.example.buycation.members.member.entity.Member;
import com.example.buycation.participant.entity.Application;
import com.example.buycation.posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Application findByPostingAndMember(Posting posting, Member member);

    List<Application> findAllByPosting(Posting posting);

    @Modifying
    @Query("delete from Application a where a in :applications")
    void deleteAllByIdInQuery(@Param("applications") List<Application> applications);
}
