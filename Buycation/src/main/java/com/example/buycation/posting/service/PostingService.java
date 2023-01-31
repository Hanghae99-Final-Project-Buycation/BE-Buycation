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
import com.example.buycation.posting.dto.MainPostingResponseDto;
import com.example.buycation.posting.dto.PostingRequestDto;
import com.example.buycation.posting.dto.PostingResponseDto;
import com.example.buycation.posting.entity.Category;
import com.example.buycation.posting.entity.Posting;
import com.example.buycation.posting.mapper.PostingMapper;
import com.example.buycation.posting.repository.PostingRepository;
import com.example.buycation.security.UserDetailsImpl;
import com.example.buycation.talk.repository.ChatRoomRepository;
import com.example.buycation.talk.service.TalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.buycation.common.exception.ErrorCode.AUTHORIZATION_DELETE_FAIL;
import static com.example.buycation.common.exception.ErrorCode.AUTHORIZATION_UPDATE_FAIL;
import static com.example.buycation.common.exception.ErrorCode.NOT_FINISH_PARTICIPATION;
import static com.example.buycation.common.exception.ErrorCode.POSTING_NOT_FOUND;
import static com.example.buycation.common.exception.ErrorCode.POSTING_RECRUITMENT_SUCCESS_ERROR;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final TalkService talkService;
    private final PostingRepository postingRepository;
    private final PostingMapper postingMapper;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ApplicationMapper applicationMapper;
    private final ParticipantRepository participantRepository;
    private final ApplicationRepository applicationRepository;

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void createPosting(PostingRequestDto postingRequestDto, Member member) {
        Posting posting = postingMapper.toPosting(postingRequestDto, member);
        postingRepository.save(posting);

        //자기자신 참가자 리스트에 등록
        Application application = applicationMapper.toApplication(member, posting);
        Participant participant = applicationMapper.toParticipant(application);
        participantRepository.save(participant);
        posting.add(participant);

        talkService.createRoom(posting);
    }

    @Transactional(readOnly = true)
    public PostingResponseDto detailPosting(Long postingId, UserDetailsImpl userDetails) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));
        //내 게시글인지 체크해주기
        boolean myPosting = false;
        //참가자인지 체크해주기
        boolean participant = false;
        if (userDetails != null) {
            if (userDetails.getMember().getId().equals(posting.getMember().getId())) {
                myPosting = true;
            }
            if (participantRepository.findByPostingAndMember(posting, userDetails.getMember()) != null) {
                participant = true;
            }
        }
        List<Comment> comments = commentRepository.findAllByPostingOrderByCreatedAtDesc(posting);
        List<CommentResponseDto> commentList = new ArrayList<>();
        for (Comment c : comments) {
            boolean status = false;
            if (userDetails != null) {
                if (c.getMember().getId().equals(userDetails.getMember().getId())) {
                    status = true;
                }
            }
            commentList.add(commentMapper.toResponse(c, status));
        }
        return postingMapper.toResponse(posting, commentList, myPosting, participant);
    }

    @Transactional
    public void finishPosting(Member member, Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));

        //인원수 채워졌는지 확인
        if (posting.getTotalMembers() != posting.getCurrentMembers()) {
            throw new CustomException(NOT_FINISH_PARTICIPATION);
        }
        //권한체크
        if (!posting.getMember().getId().equals(member.getId())) {
            throw new CustomException(AUTHORIZATION_UPDATE_FAIL);
        }

        posting.finish(true);
    }

    @Transactional
    public void updatePosting(Member member, PostingRequestDto postingRequestDto, Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));

        //완료된 게시글 수정 금지
        if (posting.isDoneStatus()) {
            throw new CustomException(POSTING_RECRUITMENT_SUCCESS_ERROR);
        }
        //권한체크
        if (!posting.getMember().getId().equals(member.getId())) {
            throw new CustomException(AUTHORIZATION_UPDATE_FAIL);
        }

        posting.update(
                postingRequestDto.getTitle(),
                postingRequestDto.getAddress(),
                postingRequestDto.getAddressDetail(),
                String.valueOf(Category.valueOf(postingRequestDto.getCategory())),
                postingRequestDto.getTotalMembers(),
                postingRequestDto.getDueDate(),
                postingRequestDto.getBudget(),
                postingRequestDto.getImage(),
                postingRequestDto.getContent(),
                postingRequestDto.getCoordsX(),
                postingRequestDto.getCoordsY()
        );
    }

    @Transactional
    public void deletePosting(Member member, Long postingId) {
        Posting posting = postingRepository.findById(postingId).orElseThrow(() -> new CustomException(POSTING_NOT_FOUND));

        //완료된 게시글 삭제 금지
        if (posting.isDoneStatus()) {
            throw new CustomException(POSTING_RECRUITMENT_SUCCESS_ERROR);
        }
        //권한체크
        if (!posting.getMember().getId().equals(member.getId())) {
            throw new CustomException(AUTHORIZATION_DELETE_FAIL);
        }

        List<Comment> comments = commentRepository.findAllByPosting(posting);
        if (!comments.isEmpty()) commentRepository.deleteAllByInQuery(comments);

        List<Application> applications = applicationRepository.findAllByPosting(posting);
        if (!applications.isEmpty()) applicationRepository.deleteAllByInQuery(applications);

        List<Participant> participants = participantRepository.findAllByPosting(posting);
        if (!participants.isEmpty()) participantRepository.deleteAllByInQuery(participants);

        chatRoomRepository.deleteByPosting(posting);

        postingRepository.deleteById(postingId);
    }

    @Transactional(readOnly = true)
    public List<MainPostingResponseDto> searchPosting(String category, String search, String sort) {
        List<Posting> postings = postingRepository.findAllByQuerydsl(category, search, sort);
        List<MainPostingResponseDto> postingList = new ArrayList<>();
        for (Posting p : postings) {
            postingList.add(postingMapper.toResponse(p));
        }
        return postingList;
    }
}
