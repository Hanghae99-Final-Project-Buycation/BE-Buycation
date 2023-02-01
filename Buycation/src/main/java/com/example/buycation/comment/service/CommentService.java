package com.example.buycation.comment.service;

import com.example.buycation.alarm.dto.RealtimeAlarmDto;
import com.example.buycation.alarm.entity.AlarmType;
import com.example.buycation.alarm.service.AlarmService;
import com.example.buycation.comment.dto.CommentRequestDto;
import com.example.buycation.comment.entity.Comment;
import com.example.buycation.comment.mapper.CommentMapper;
import com.example.buycation.comment.repository.CommentRepository;
import com.example.buycation.common.exception.CustomException;
import com.example.buycation.common.exception.ErrorCode;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.posting.entity.Posting;
import com.example.buycation.posting.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.buycation.common.exception.ErrorCode.AUTHORIZATION_DELETE_FAIL;
import static com.example.buycation.common.exception.ErrorCode.AUTHORIZATION_UPDATE_FAIL;
import static com.example.buycation.common.exception.ErrorCode.COMMENT_NOT_FOUND;
import static com.example.buycation.common.exception.ErrorCode.POSTING_NOT_FOUND;
import static com.example.buycation.common.exception.ErrorCode.POSTING_RECRUITMENT_SUCCESS_ERROR;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostingRepository postingRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void createComment(CommentRequestDto commentRequestDto, Member member, Long postingId) {

        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));

        //완료된 게시글 댓글작성 금지
        if(posting.isDoneStatus()){
            throw new CustomException(POSTING_RECRUITMENT_SUCCESS_ERROR);
        }

        Comment comment = commentMapper.toComment(commentRequestDto, member, posting);
        commentRepository.save(comment);
        posting.add(comment);

        try {
            if (!posting.getMember().getId().equals(member.getId())) {
                //alarmService.createAlarm(posting.getMember(), AlarmType.COMMENT, postingId, posting.getTitle());
                applicationEventPublisher.publishEvent(RealtimeAlarmDto.builder()
                        .postingId(postingId)
                        .alarmType(AlarmType.COMMENT)
                        .member(posting.getMember())
                        .title(posting.getTitle()).build());
            }
        } catch(Exception e){
           System.out.println(ErrorCode.ALARM_NOT_FOUND);
        }
    }

    @Transactional
    public void updateComment(CommentRequestDto commentRequestDto, Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));

        //완료된 게시글 댓글수정 금지
        if(comment.getPosting().isDoneStatus()){
            throw new CustomException(POSTING_RECRUITMENT_SUCCESS_ERROR);
        }
        //권한체크
        if (!comment.getMember().getId().equals(member.getId())){
            throw new CustomException(AUTHORIZATION_UPDATE_FAIL);
        }
        comment.update(commentRequestDto.getContent());
    }

    @Transactional
    public void deleteComment(Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));

        //완료된 게시글 댓글삭제 금지
        if(comment.getPosting().isDoneStatus()){
            throw new CustomException(POSTING_RECRUITMENT_SUCCESS_ERROR);
        }
        //권한체크
        if (!comment.getMember().getId().equals(member.getId())){
            throw new CustomException(AUTHORIZATION_DELETE_FAIL);
        }
        commentRepository.delete(comment);
    }
}
