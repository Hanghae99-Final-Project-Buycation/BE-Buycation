package com.example.buycation.comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
    private Long commentId;
    private Long memberId;
    private String nickname;
    private String content;
    private String createdAt;
    private boolean status;
}
