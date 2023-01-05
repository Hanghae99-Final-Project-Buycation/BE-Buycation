package com.example.buycation.comment.service;

import com.example.buycation.comment.dto.CommentRequestDto;
import com.example.buycation.comment.entity.Comment;
import com.example.buycation.comment.mapper.CommentMapper;
import com.example.buycation.comment.repository.CommentRepository;
import com.example.buycation.common.exception.CustomException;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.posting.entity.Posting;
import com.example.buycation.posting.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.buycation.common.exception.ErrorCode.AUTHORIZATION_UPDATE_FAIL;
import static com.example.buycation.common.exception.ErrorCode.COMMENT_NOT_FOUND;
import static com.example.buycation.common.exception.ErrorCode.POSTING_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostingRepository postingRepository;


    @Transactional
    public void createComment(CommentRequestDto commentRequestDto, Member member, Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));
        Comment comment = commentMapper.toComment(commentRequestDto, member, posting);
        commentRepository.save(comment);
        posting.add(comment);
    }

    @Transactional
    public void updateComment(CommentRequestDto commentRequestDto, Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));
        if (!comment.getMember().equals(member)){
            throw new CustomException(AUTHORIZATION_UPDATE_FAIL);
        }
        comment.update(commentRequestDto.getContent());
    }
}
