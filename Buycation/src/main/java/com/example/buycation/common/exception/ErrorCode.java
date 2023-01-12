package com.example.buycation.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER("파라미터 값을 확인해주세요.", 400),
    INVALID_EMAIL_PATTERN("id는 소문자와 숫자 조합 4자리에서 10자리입니다.",400),
    INVALID_PASSWORD_PATTERN("비밀번호는 소문자, 대문자, 숫자, 특수문자(!@#$%^&+=) 조합 8자리에서 15자리입니다.",400),
    INVALID_NICKNAME_PATTERN("닉네임은 영문, 숫자, 한글(자음, 모음 단위 x) 조합에 2~10자입니다.",400),
    DUPLICATE_EMAIL("이미 존재하는 이메일입니다.", 400),
    DUPLICATE_NICKNAME("중복된 닉네임이 존재합니다.", 400),
    REQUIRED_ALL("모든 항목이 필수값입니다.",400),
    WRONG_IMAGE_FORMAT("파일을 확인해주세요.", 400),
    PASSWORD_MISMATCH("비밀번호가 비밀번호 확인과 일치하지 않습니다",400),
    DUPLICATE_APPLICATION("이미 지원한 유저입니다.", 400),
    DUPLICATE_PARTICIPATION("이미 참가한 유저입니다.", 400),
    FINISH_PARTICIPATION("목표 참가자 수만큼 모집이되었입니다.", 400),
    POSTING_RECRUITMENT_SUCCESS_ERROR("모집이 완료된 게시글입니다.",400),
    SELF_REVIEW_ERROR("자기 자신에게 후기를 남길 수 없습니다.",400),
    POSTING_PARTICIPANT_REVIEW("게시글 참가자만 리뷰를 남길 수 있습니다.",400),
    DUPLICATE_REVIEW("같은 멤버에게 중복 리뷰는 불가능합니다.",400),
    POSTING_SUCCESS_ERROR("구인 완료가 되지않은 게시물입니다.",400),
    WRONG_CATEGORY_ERROR("잘못된 카테고리입니다.",400),
    WRONG_SORT_ERROR("잘못된 정렬입니다.",400),
    EMAIL_SEND_FAIL("이메일 발송에 실패하였습니다..",400),

    //404 NOT_FOUND 잘못된 리소스 접근
    APPLICANT_NOT_FOUND("존재하지 않는 지원자 입니다.",404),
    POSTING_NOT_FOUND("존재하지 않는 게시글 입니다.",404),
    COMMENT_NOT_FOUND("존재하지 않는 댓글 입니다.", 404),
    USERNAME_NOT_FOUND("존재하지 않는 아이디 입니다.",404),
    MEMBER_NOT_FOUND("존재하지 않는 아이디 입니다.",404),
    PARTICIPANT_NOT_FOUND("존재하지 않는 참가자 입니다.",404),
    INCORRECT_PASSWORD("비밀번호가 일치하지 않습니다.",404),

    //401 잘못된 권한 접근
    AUTHORIZATION_DELETE_FAIL("삭제 권한이 없습니다.", 401),
    AUTHORIZATION_UPDATE_FAIL("수정 권한이 없습니다.", 401),
    AUTHORIZATION_DECISION_FAIL("결정 권한이 없습니다.", 401),
    AUTHORIZATION_LOOKUP_FAIL("조회 권한이 없습니다.", 401),
    AUTHORIZATION_APPLICANT_LOOKUP_FAIL("지원자 조회 권한이 없습니다.", 401),
    WRITER_PARTICIPATION_CANAEL("작성자는 참가취소를 할 수 없습니다. 게시글을 삭제하여 주세요.", 401),

    //필터부분 에러
    FORBIDDEN_ERROR("서버 사용 권한이 없습니다.",403),
    TOKEN_ERROR("토큰이 유효하지 않습니다.",401),
    USER_NOT_FOUND("존재하지 않는 유저 입니다.",404),

    INTERNAL_SERVER_ERROR("서버 에러입니다. 서버 팀에 연락주세요!", 500);


    private final String msg;
    private final int statusCode;
}