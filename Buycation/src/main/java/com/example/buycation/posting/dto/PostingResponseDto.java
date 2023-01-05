package com.example.buycation.posting.dto;

import com.example.buycation.comment.dto.CommentResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PostingResponseDto {
    private Long memberId;
    private String nickname;
    private Long postingId;
    private String title;
    private String address;
    private String content;
    private String image;
    private String dueDate;
    private long budget;
    private int totalMembers;
    private int currentMembers;
    private String createdAt;
    private String category;
    private List<CommentResponseDto> commentList;
    private Boolean doneStatus;
}
