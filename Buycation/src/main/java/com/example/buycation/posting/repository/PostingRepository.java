package com.example.buycation.posting.repository;

import com.example.buycation.members.member.entity.Member;
import com.example.buycation.posting.entity.Posting;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostingRepository extends JpaRepository<Posting, Long> {
    List<Posting> findAllByMember(Member member);

    List<Posting> findAllByOrderByCreatedAtDesc();

    @Query("SELECT p FROM Posting p " +
            "where (p.title like CONCAT('%', :search,'%') or p.address like CONCAT('%', :search,'%')) " +
            "and p.category like CONCAT('%', :category,'%')")
    List<Posting> findAllByQuery(@Param("category") String category,
                                 @Param("search") String search,
                                 Sort sort);
}
