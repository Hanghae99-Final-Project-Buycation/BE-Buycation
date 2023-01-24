package com.example.buycation.posting.mapper;

import com.example.buycation.comment.dto.CommentResponseDto;
import com.example.buycation.members.member.entity.Member;
import com.example.buycation.posting.dto.MainPostingResponseDto;
import com.example.buycation.posting.dto.PostingRequestDto;
import com.example.buycation.posting.dto.PostingResponseDto;
import com.example.buycation.posting.entity.Category;
import com.example.buycation.posting.entity.Posting;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PostingMapper {

    public PostingResponseDto toResponse(Posting posting, List<CommentResponseDto> commentList, boolean myPosting, boolean participant){
        return PostingResponseDto.builder()
                .memberId(posting.getMember().getId())
                .nickname(posting.getMember().getNickname())
                .profileImage(posting.getMember().getProfileImage())
                .postingId(posting.getId())
                .title(posting.getTitle())
                .address(posting.getAddress())
                .addressDetail(posting.getAddressDetail())
                .content(posting.getContent())
                .image(posting.getImage())
                .dueDate(posting.getDueDate())
                .budget(posting.getBudget())
                .perBudget((posting.getBudget())/(posting.getTotalMembers()))
                .totalMembers(posting.getTotalMembers())
                .currentMembers(posting.getCurrentMembers())
                .createdAt(posting.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .category(posting.getCategory())
                .commentList(commentList)
                .doneStatus(posting.isDoneStatus())
                .myPosting(myPosting)
                .participant(participant)
                .build();
    }

    public MainPostingResponseDto toResponse(Posting posting){
        return MainPostingResponseDto.builder()
                .postingId(posting.getId())
                .title(posting.getTitle())
                .address(posting.getAddress())
                .image(posting.getImage())
                .dueDate(posting.getDueDate())
                .perBudget(posting.getPerBudget())
                .totalMembers(posting.getTotalMembers())
                .currentMembers(posting.getCurrentMembers())
                .category(posting.getCategory())
                .doneStatus(posting.isDoneStatus())
                .coordsX(posting.getCoordsX())
                .coordsY(posting.getCoordsY())
                .build();
    }

    public Posting toPosting(PostingRequestDto postingRequestDto, Member member){
        return Posting.builder()
                .title(postingRequestDto.getTitle())
                .category(String.valueOf(Category.valueOf(postingRequestDto.getCategory())))
                .totalMembers(postingRequestDto.getTotalMembers())
                .address(postingRequestDto.getAddress())
                .addressDetail(postingRequestDto.getAddressDetail())
                .budget(postingRequestDto.getBudget())
                .perBudget(postingRequestDto.getBudget()/postingRequestDto.getTotalMembers())
                .dueDate(postingRequestDto.getDueDate())
                .image(postingRequestDto.getImage())
                .content(postingRequestDto.getContent())
                .member(member)
                .doneStatus(false)
                .currentMembers(1)
                .coordsX(postingRequestDto.getCoordsX())
                .coordsY(postingRequestDto.getCoordsY())
                .build();
    }
}
