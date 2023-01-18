package com.example.buycation.alarm.repository;

import com.example.buycation.alarm.entity.Alarm;
import com.example.buycation.members.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findAllByMemberOrderByMemberDesc(Member member);
    Long countByReadFalseAndMember(Member member);

    void deleteAllByMember(Member member);
}
