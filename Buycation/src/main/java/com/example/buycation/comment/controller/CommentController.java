package com.example.buycation.comment.controller;

import com.example.buycation.comment.dto.CommentRequestDto;
import com.example.buycation.comment.service.CommentService;
import com.example.buycation.common.ResponseMessage;
import com.example.buycation.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.buycation.common.MessageCode.COMMENT_CREATE_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posting")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postingId}/comments")
    public ResponseMessage<?> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody CommentRequestDto commentRequestDto,
                                            @PathVariable Long postingId) {
        commentService.createComment(commentRequestDto, userDetails.getMember(), postingId);
        return new ResponseMessage<>(COMMENT_CREATE_SUCCESS, null);
    }
}
