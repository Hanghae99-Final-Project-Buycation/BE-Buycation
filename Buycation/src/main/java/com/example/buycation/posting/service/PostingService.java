package com.example.buycation.posting.service;

import com.example.buycation.comment.dto.CommentResponseDto;
import com.example.buycation.comment.entity.Comment;
import com.example.buycation.comment.mapper.CommentMapper;
import com.example.buycation.comment.repository.CommentRepository;
import com.example.buycation.common.exception.CustomException;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.posting.dto.PostingRequestDto;
import com.example.buycation.posting.dto.PostingResponseDto;
import com.example.buycation.posting.entity.Posting;
import com.example.buycation.posting.mapper.PostingMapper;
import com.example.buycation.posting.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.buycation.common.exception.ErrorCode.POSTING_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;
    private final PostingMapper postingMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public void createPosting(PostingRequestDto postingRequestDto, Member member) {
        Posting posting = postingMapper.toPosting(postingRequestDto, member);
        postingRepository.save(posting);
    }


    @Transactional(readOnly = true)
    public PostingResponseDto detailPosting(Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));
        List<Comment> comments = commentRepository.findAllByPostingOrderByCreatedAtDesc(posting);
        List<CommentResponseDto> commentList = new ArrayList<>();
        for (Comment c : comments) {
            commentList.add(commentMapper.toResponse(c));
        }
        return postingMapper.toResponse(posting, commentList);
    }
}
