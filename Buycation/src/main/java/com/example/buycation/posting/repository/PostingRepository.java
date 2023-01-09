package com.example.buycation.posting.repository;

import com.example.buycation.members.member.entity.Member;
import com.example.buycation.posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostingRepository extends JpaRepository<Posting, Long> {
    List<Posting> findAllByMember(Member member);
}
