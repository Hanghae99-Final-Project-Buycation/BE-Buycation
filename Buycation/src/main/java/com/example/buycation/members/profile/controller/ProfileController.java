package com.example.buycation.members.profile.controller;

import com.example.buycation.common.ResponseMessage;
import com.example.buycation.members.profile.dto.ReviewRequestDto;
import com.example.buycation.members.profile.service.ProfileService;
import com.example.buycation.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.buycation.common.MessageCode.REVIEW_WRITE_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/posting/{postingId}/review/{memberId}")
    public ResponseMessage<?> createPosting(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody ReviewRequestDto reviewRequestDto,
                                            @PathVariable Long postingId,
                                            @PathVariable Long memberId) {
        profileService.createReview(reviewRequestDto, userDetails.getMember(), postingId, memberId);
        return new ResponseMessage<>(REVIEW_WRITE_SUCCESS, null);
    }
}
