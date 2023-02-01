package com.example.buycation.posting.repository;

import com.example.buycation.members.member.entity.Member;
import com.example.buycation.posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostingRepository extends JpaRepository<Posting, Long>, PostingRepositoryCustom {
    List<Posting> findAllByMemberOrderByCreatedAtDesc(Member member);

    @Query("SELECT p FROM Posting p where p.doneStatus = :status and p.dueDate <= :currentDateTime")
    List<Posting> findUpdateData(@Param("currentDateTime")String currentDateTime,
                                 @Param("status")boolean status);

    @Modifying
    @Query("delete from Posting p where p in :postings")
    void deleteAllByIdInQuery(@Param("postings") List<Posting> postings);
}
