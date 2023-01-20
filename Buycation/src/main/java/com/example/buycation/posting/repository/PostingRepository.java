package com.example.buycation.posting.repository;

import com.example.buycation.members.member.entity.Member;
import com.example.buycation.posting.entity.Posting;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostingRepository extends JpaRepository<Posting, Long> {
    List<Posting> findAllByMember(Member member);

    @Query("SELECT p FROM Posting p " +
            "where (p.title like CONCAT('%', :search,'%') escape '|' or p.address like CONCAT('%', :search,'%') escape '|') " +
            "and p.category like CONCAT('%', :category,'%') and p.doneStatus = false ")
    List<Posting> findAllByQuery(@Param("category") String category,
                                 @Param("search") String search,
                                 Sort sort);

    @Query("SELECT p FROM Posting p where p.doneStatus = false and p.dueDate <= :currentDateTime")
    List<Posting> findUpdateData(@Param("currentDateTime")String currentDateTime);

    @Query("SELECT p FROM Posting p where p.doneStatus = false and p.dueDate = :currentDateTime")
    List<Posting> findAllByDueDateBefore30Minute(@Param("currentDateTime")String currentDateTime);

    @Modifying
    @Query("delete from Posting p where p.id in :PostingIds")
    void deleteAllByIdInQuery(@Param("PostingIds") List<Long> PostingIds);
}
