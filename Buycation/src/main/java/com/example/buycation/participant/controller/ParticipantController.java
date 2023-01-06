package com.example.buycation.participant.controller;

import com.example.buycation.common.ResponseMessage;
import com.example.buycation.participant.dto.ApplicationResponseDto;
import com.example.buycation.participant.service.ParticipantService;
import com.example.buycation.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.buycation.common.MessageCode.APPLICATION_ACCEPT_SUCCESS;
import static com.example.buycation.common.MessageCode.APPLICATION_LIST_LOOKUP_SUCCESS;
import static com.example.buycation.common.MessageCode.APPLICATION_REFUSE_SUCCESS;
import static com.example.buycation.common.MessageCode.POSTING_APPLICATION_CANCEL_SUCCESS;
import static com.example.buycation.common.MessageCode.POSTING_APPLICATION_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/participant")
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/posting/{postingId}")
    public ResponseMessage<?> createApplicant(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable Long postingId) {
        participantService.createApplicant(userDetails.getMember(), postingId);
        return new ResponseMessage<>(POSTING_APPLICATION_SUCCESS, null);
    }

    @GetMapping("/posting/{postingId}")
    public ResponseMessage<List<ApplicationResponseDto>> getApplicant(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @PathVariable Long postingId) {
        List<ApplicationResponseDto> applicationResponseDtoList = participantService.getApplicant(userDetails.getMember(), postingId);
        return new ResponseMessage<>(APPLICATION_LIST_LOOKUP_SUCCESS, applicationResponseDtoList);
    }

    @PostMapping("/{applicationId}/posting/{postingId}")
    public ResponseMessage<?> applicantAccept(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable Long postingId,
                                              @PathVariable Long applicationId) {
        participantService.applicantAccept(userDetails.getMember(), postingId, applicationId);
        return new ResponseMessage<>(APPLICATION_ACCEPT_SUCCESS, null);
    }

    @DeleteMapping("/{applicationId}/posting/{postingId}")
    public ResponseMessage<?> applicantRefuse(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable Long postingId,
                                              @PathVariable Long applicationId) {
        participantService.applicantRefuse(userDetails.getMember(), postingId, applicationId);
        return new ResponseMessage<>(APPLICATION_REFUSE_SUCCESS, null);
    }

    @DeleteMapping("/posting/{postingId}")
    public ResponseMessage<?> cancelParticipation(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                              @PathVariable Long postingId) {
        participantService.cancelParticipation(userDetails.getMember(), postingId);
        return new ResponseMessage<>(POSTING_APPLICATION_CANCEL_SUCCESS, null);
    }
}
