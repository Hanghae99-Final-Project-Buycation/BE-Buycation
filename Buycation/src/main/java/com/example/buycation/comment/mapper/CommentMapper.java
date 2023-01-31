package com.example.buycation.comment.mapper;

import com.example.buycation.comment.dto.CommentRequestDto;
import com.example.buycation.comment.dto.CommentResponseDto;
import com.example.buycation.comment.entity.Comment;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.posting.entity.Posting;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class CommentMapper {

    public CommentResponseDto toResponse(Comment comment, boolean status){
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .memberId(comment.getMember().getId())
                .nickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .status(status)
                .build();
    }
    public Comment toComment(CommentRequestDto commentRequestDto, Member member, Posting posting){
        return Comment.builder()
                .content(commentRequestDto.getContent())
                .member(member)
                .posting(posting)
                .build();
    }
}
