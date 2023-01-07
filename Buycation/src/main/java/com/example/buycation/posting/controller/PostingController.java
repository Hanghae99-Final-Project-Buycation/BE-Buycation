package com.example.buycation.posting.controller;

import com.example.buycation.common.ResponseMessage;
import com.example.buycation.posting.dto.PostingRequestDto;
import com.example.buycation.posting.dto.PostingResponseDto;
import com.example.buycation.posting.service.PostingService;
import com.example.buycation.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.buycation.common.MessageCode.POSTING_CREATE_SUCCESS;
import static com.example.buycation.common.MessageCode.POSTING_DELETE_SUCCESS;
import static com.example.buycation.common.MessageCode.POSTING_LOOKUP_SUCCESS;
import static com.example.buycation.common.MessageCode.POSTING_RECRUITMENT_SUCCESS;
import static com.example.buycation.common.MessageCode.POSTING_UPDATE_SUCCESS;

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
    public ResponseMessage<PostingResponseDto> detailPosting(@PathVariable Long postingId){
        PostingResponseDto postingResponseDto = postingService.detailPosting(postingId);
        return new ResponseMessage<>(POSTING_LOOKUP_SUCCESS, postingResponseDto);
    }

    @PatchMapping("/{postingId}")
    public ResponseMessage<?> finishPosting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable Long postingId) {
        postingService.finishPosting(userDetails.getMember(), postingId);
        return new ResponseMessage<>(POSTING_RECRUITMENT_SUCCESS, null);
    }

    @PutMapping("/{postingId}")
    public ResponseMessage<?> updatePosting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody PostingRequestDto postingRequestDto,
                                            @PathVariable Long postingId) {
        postingService.updatePosting(userDetails.getMember(), postingRequestDto, postingId);
        return new ResponseMessage<>(POSTING_UPDATE_SUCCESS, null);
    }

    @DeleteMapping("/{postingId}")
    public ResponseMessage<?> deletePosting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @PathVariable Long postingId) {
        postingService.deletePosting(userDetails.getMember(), postingId);
        return new ResponseMessage<>(POSTING_DELETE_SUCCESS, null);
    }
}
