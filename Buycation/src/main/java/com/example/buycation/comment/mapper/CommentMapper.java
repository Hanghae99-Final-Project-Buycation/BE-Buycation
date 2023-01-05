package com.example.buycation.comment.mapper;

import com.example.buycation.comment.dto.CommentRequestDto;
import com.example.buycation.comment.entity.Comment;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.posting.entity.Posting;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public Comment toComment(CommentRequestDto commentRequestDto, Member member, Posting posting){
        return Comment.builder()
                .content(commentRequestDto.getContent())
                .member(member)
                .posting(posting)
                .build();
    }
}
