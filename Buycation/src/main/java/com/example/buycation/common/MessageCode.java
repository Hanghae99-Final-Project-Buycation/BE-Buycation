package com.example.buycation.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageCode {

    SUCCESS("성공",200),

    //회원가입, 로그인 관련
    MEMBER_SIGNUP_SUCCESS("회원가입에 성공하였습니다",200),
    EMAIL_CHECK_SUCCESS("사용가능한 이메일입니다.",200),
    NICKNAME_CHECK_SUCCESS("사용가능한 닉네임입니다.",200),
    MEMBER_LOGIN_SUCCESS("로그인되었습니다.",200),
    EMAIL_CONFIRM_CODE_SUCCESS("이메일 확인코드 전달성공",200),
    EMAIL_CONFIRM_CODE_CHECK_SUCCESS("이메일 확인코드 확인성공.",200),

    //게시글 관련
    POSTING_LOOKUP_SUCCESS("게시글 조회에 성공하였습니다.",200),
    CATEGORY_LOOKUP_SUCCESS("카테고리 조회에 성공하였습니다.",200),
    POSTING_CREATE_SUCCESS("게시글 작성에 성공하였습니다.",200),
    POSTING_RECRUITMENT_SUCCESS("모집을 완료하였습니다.",200),
    POSTING_UPDATE_SUCCESS("게시글 수정에 성공하였습니다.",200),
    POSTING_DELETE_SUCCESS("게시글 삭제에 성공하였습니다.",200),

    //게시글 참가신청 관련
    APPLICATION_LIST_LOOKUP_SUCCESS("게시글 지원자 리스트 조회에 성공하였습니다.",200),
    POSTING_APPLICATION_SUCCESS("해당 게시글에 참가 신청하였습니다.",200),
    APPLICATION_ACCEPT_SUCCESS("신청을 수락하였습니다.",200),
    APPLICATION_REFUSE_SUCCESS("신청을 거절하였습니다.",200),
    POSTING_APPLICATION_CANCEL_SUCCESS("해당 게시글에 참가 취소하였습니다.",200),

    //댓글 관련
    COMMENT_CREATE_SUCCESS("댓글 작성에 성공하였습니다.",200),
    COMMENT_MODIFY_SUCCESS("댓글 수정에 성공하였습니다.",200),
    COMMENT_DELETE_SUCCESS("댓글 삭제에 성공하였습니다.",200),

    //프로필 관련
    MEMBER_LOOKUP_SUCCESS("프로필 조회에 성공하였습니다.",200),
    MEMBER_UPDATE_SUCCESS("프로필 수정에 성공하였습니다.",200),
    MY_POSTING_LOOKUP_SUCCESS("내 게시글 조회에 성공하였습니다.",200),
    PARTICIPATION_POSTING_LOOKUP_SUCCESS("참여 게시글 조회에 성공하였습니다.",200),
    REVIEW_LIST_LOOKUP_SUCCESS("평점 목록 조회에 성공하였습니다.",200),
    REVIEW_WRITE_SUCCESS("평점 남기기에 성공하였습니다.",200),

    //알람 관련
    ALARM_SEARCH_SUCCESS("알림목록 조회에 성공하였습니다.", 200),
    ALARM_READ_SUCCESS("알림 읽기 처리에 성공하였습니다.", 200),
    ALARM_DELETE_SUCCESS("알림 삭제에 성공하였습니다.", 200),
    ALARM_COUNT_SUCCESS("알림 갯수 조회 성공하였습니다.", 200);
    private final String msg;
    private final int statusCode;
}
