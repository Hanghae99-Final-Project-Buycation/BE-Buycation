package com.example.buycation.alarm.repository;

import com.example.buycation.alarm.entity.Alarm;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.posting.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findAllByMemberOrderByMemberDesc(Member member);
    List<Alarm> findTop15ByIdLessThanAndMemberOrderByIdDesc(Long id, Member member);
    List<Alarm> findTop15ByMemberOrderByIdDesc(Member member);

    List<Alarm> findTop3ByIdLessThanAndMemberOrderByIdDesc(Long id, Member member);
    List<Alarm> findTop3ByMemberOrderByIdDesc(Member member);
    Long countByIsReadFalseAndMember(Member member);


    void deleteAllByMember(Member member);

    @Modifying
    @Query(value = "DELETE FROM Alarm where created_at < :targetDateTime", nativeQuery = true)
    void deleteAlarmByDueDateBeforeAMonth(@Param("targetDateTime") LocalDateTime targetDateTime);
}
