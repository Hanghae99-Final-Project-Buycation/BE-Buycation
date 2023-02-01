package com.example.buycation.alarm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AlarmType {
    CHAT(""),
    COMMENT("내가 만든 공구에 댓글이 달렸습니다."),
    APPLICATION("공구에 참가신청이 도착했습니다."),
    ACCEPT("공구 신청이 수락되었습니다."),
    REJECT("공구 신청이 거절되었습니다."),
    DONE("공구 모집이 완료되었습니다."),
    DELETE("해당 공구가 취소되었습니다."),
    FAIL("해당 공구의 모집이 실패했습니다."),
    UPDATE("해당 공구의 내용이 변경되었습니다."),
    REMIND("공구 만료까지 1시간 남았습니다.");



    private final String message;
}
