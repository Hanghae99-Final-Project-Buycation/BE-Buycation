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

//    List<Posting> findByTitleContainingIgnoreCaseOrAddressContainingIgnoreCaseOrderByCreatedAtDesc(String title, String address);
//
//    List<Posting> findByTitleContainingIgnoreCaseOrAddressContainingIgnoreCaseOrderByTotalMembersDesc(String search, String search1);
//
//    List<Posting> findByTitleContainingIgnoreCaseOrAddressContainingIgnoreCaseOrderByDueDateDesc(String search, String search1);
//
//    @Modifying
//    @Query("SELECT p.id, p.title, p.image, p.dueDate , p.budget/p.totalMembers as perBudget, p.currentMembers, p.address, p.category FROM Posting p\n" +
//            "where p.category like :category\n" +
//            "and p.title like :title or p.address like :address\n" +
//            "order by :sort desc")
//    List<Posting> findAllByQuery(@Param("category")String category,
//                        @Param("title")String title,
//                        @Param("address")String address,
//                        @Param("sort")String sort);

    @Query("SELECT p FROM Posting p " +
            "where (p.title like CONCAT('%', :search,'%') or p.address like CONCAT('%', :search,'%')) " +
            "and p.category like CONCAT('%', :category,'%')")
    List<Posting> findAllByQuery(@Param("category") String category,
                                 @Param("search") String search,
                                 Sort sort);
}
