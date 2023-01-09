package com.example.buycation.members.profile.controller;

import com.example.buycation.common.ResponseMessage;
import com.example.buycation.members.profile.dto.ReviewRequestDto;
import com.example.buycation.members.profile.dto.MemberReviewResponseDto;
import com.example.buycation.members.profile.service.ProfileService;
import com.example.buycation.posting.dto.MainPostingResponseDto;
import com.example.buycation.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.buycation.common.MessageCode.MY_POSTING_LOOKUP_SUCCESS;
import static com.example.buycation.common.MessageCode.PARTICIPATION_POSTING_LOOKUP_SUCCESS;
import static com.example.buycation.common.MessageCode.REVIEW_LIST_LOOKUP_SUCCESS;
import static com.example.buycation.common.MessageCode.REVIEW_WRITE_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/posting/{postingId}")
    public ResponseMessage<List<MemberReviewResponseDto>> getMemberReviewList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                              @PathVariable Long postingId) {
        List<MemberReviewResponseDto> memberReviewResponseDtoList = profileService.getMemberReviewList(postingId, userDetails.getMember());
        return new ResponseMessage<>(REVIEW_LIST_LOOKUP_SUCCESS, memberReviewResponseDtoList);
    }

    @PostMapping("/posting/{postingId}/review/{memberId}")
    public ResponseMessage<?> createPosting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody ReviewRequestDto reviewRequestDto,
                                            @PathVariable Long postingId,
                                            @PathVariable Long memberId) {
        profileService.createReview(reviewRequestDto, userDetails.getMember(), postingId, memberId);
        return new ResponseMessage<>(REVIEW_WRITE_SUCCESS, null);
    }

    @GetMapping("/{memberId}/myposting")
    public ResponseMessage<List<MainPostingResponseDto>> getMyPostingList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                          @PathVariable Long memberId) {
        List<MainPostingResponseDto> postingList = profileService.getMyPostingList(userDetails.getMember(), memberId);
        return new ResponseMessage<>(MY_POSTING_LOOKUP_SUCCESS, postingList);
    }

    @GetMapping("/{memberId}/joinposting")
    public ResponseMessage<List<MainPostingResponseDto>> getParticipationPostingList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                     @PathVariable Long memberId) {
        List<MainPostingResponseDto> postingList = profileService.getParticipationPostingList(userDetails.getMember(), memberId);
        return new ResponseMessage<>(PARTICIPATION_POSTING_LOOKUP_SUCCESS, postingList);
    }

}
