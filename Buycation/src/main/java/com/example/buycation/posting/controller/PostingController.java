package com.example.buycation.posting.controller;

import com.example.buycation.common.ResponseMessage;
import com.example.buycation.posting.dto.PostingRequestDto;
import com.example.buycation.posting.dto.PostingResponseDto;
import com.example.buycation.posting.service.PostingService;
import com.example.buycation.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.buycation.common.MessageCode.POSTING_CREATE_SUCCESS;
import static com.example.buycation.common.MessageCode.POSTING_LOOKUP_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posting")
public class PostingController {

    private final PostingService postingService;

    @PostMapping("")
    public ResponseMessage<?> createPosting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody PostingRequestDto postingRequestDto) {
        postingService.createPosting(postingRequestDto, userDetails.getMember());
        return new ResponseMessage<>(POSTING_CREATE_SUCCESS, null);
    }

    @GetMapping("/{postingId}")
    public ResponseMessage<?> detailPosting(@PathVariable Long postingId){
        PostingResponseDto postingResponseDto = postingService.detailPosting(postingId);
        return new ResponseMessage<>(POSTING_LOOKUP_SUCCESS, postingResponseDto);
    }
}
