package com.example.buycation.posting.service;

import com.example.buycation.comment.dto.CommentResponseDto;
import com.example.buycation.comment.entity.Comment;
import com.example.buycation.comment.mapper.CommentMapper;
import com.example.buycation.comment.repository.CommentRepository;
import com.example.buycation.common.exception.CustomException;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.participant.entity.Application;
import com.example.buycation.participant.entity.Participant;
import com.example.buycation.participant.mapper.ApplicationMapper;
import com.example.buycation.participant.repository.ApplicationRepository;
import com.example.buycation.participant.repository.ParticipantRepository;
import com.example.buycation.posting.dto.PostingRequestDto;
import com.example.buycation.posting.dto.PostingResponseDto;
import com.example.buycation.posting.entity.Category;
import com.example.buycation.posting.entity.Posting;
import com.example.buycation.posting.mapper.PostingMapper;
import com.example.buycation.posting.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.buycation.common.exception.ErrorCode.AUTHORIZATION_DELETE_FAIL;
import static com.example.buycation.common.exception.ErrorCode.AUTHORIZATION_UPDATE_FAIL;
import static com.example.buycation.common.exception.ErrorCode.POSTING_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;
    private final PostingMapper postingMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ApplicationMapper applicationMapper;
    private final ParticipantRepository participantRepository;
    private final ApplicationRepository applicationRepository;

    @Transactional
    public void createPosting(PostingRequestDto postingRequestDto, Member member) {
        Posting posting = postingMapper.toPosting(postingRequestDto, member);
        postingRepository.save(posting);

        Application application = applicationMapper.toApplication(member, posting);
        Participant participant = applicationMapper.toParticipant(application);
        participantRepository.save(participant);
        posting.add(participant);
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

    @Transactional
    public void finishPosting(Member member, Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));

        if (!posting.getMember().getId().equals(member.getId())) {
            throw new CustomException(AUTHORIZATION_UPDATE_FAIL);
        }

        posting.finish(true);
    }

    @Transactional
    public void updatePosting(Member member, PostingRequestDto postingRequestDto, Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));

        if (!posting.getMember().getId().equals(member.getId())) {
            throw new CustomException(AUTHORIZATION_UPDATE_FAIL);
        }

        posting.update(postingRequestDto.getTitle(),
                postingRequestDto.getAddress(),
                String.valueOf(Category.valueOf(postingRequestDto.getCategory())),
                postingRequestDto.getTotalMembers(),
                postingRequestDto.getDueDate(),
                postingRequestDto.getBudget(),
                postingRequestDto.getImage(),
                postingRequestDto.getContent());
    }

    @Transactional
    public void deletePosting(Member member, Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));

        if (!posting.getMember().getId().equals(member.getId())) {
            throw new CustomException(AUTHORIZATION_DELETE_FAIL);
        }

        List<Comment> comments = commentRepository.findAllByPosting(posting);
        if (!comments.isEmpty()){commentRepository.deleteAllByIdInQuery(comments);}
        List<Application> applications = applicationRepository.findAllByPosting(posting);
        if (!applications.isEmpty()){applicationRepository.deleteAllByIdInQuery(applications);}
        List<Participant> participants = participantRepository.findAllByPosting(posting);
        if (!participants.isEmpty()){participantRepository.deleteAllByIdInQuery(participants);}

        postingRepository.deleteById(postingId);
    }
}
