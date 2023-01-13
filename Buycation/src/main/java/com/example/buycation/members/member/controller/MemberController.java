package com.example.buycation.members.member.controller;

import com.example.buycation.common.MessageCode;
import com.example.buycation.common.ResponseMessage;
import com.example.buycation.members.member.dto.LoginRequestDto;
import com.example.buycation.members.member.dto.MemberResponseDto;
import com.example.buycation.members.member.dto.SignupRequestDto;
import com.example.buycation.members.member.dto.UpdateMemberRequestDto;
import com.example.buycation.members.member.service.MemberService;
import com.example.buycation.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.example.buycation.common.MessageCode.MEMBER_LOGIN_SUCCESS;
import static com.example.buycation.common.MessageCode.MEMBER_LOOKUP_SUCCESS;
import static com.example.buycation.common.MessageCode.MEMBER_SIGNUP_SUCCESS;
import static com.example.buycation.common.MessageCode.MEMBER_UPDATE_SUCCESS;
import static com.example.buycation.common.MessageCode.NICKNAME_CHECK_SUCCESS;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseMessage<MessageCode> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        memberService.signup(signupRequestDto);
        return new ResponseMessage<>(MEMBER_SIGNUP_SUCCESS, null);
    }

    @PostMapping("/login")
    public ResponseMessage<MessageCode> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        memberService.login(loginRequestDto, response);
        return new ResponseMessage<>(MEMBER_LOGIN_SUCCESS, null);
    }

    @GetMapping("/signup")
    public ResponseMessage<MessageCode> checkNickname(@RequestParam("nickname") String nickname) {
        memberService.checkNickname(nickname);
        return new ResponseMessage<>(NICKNAME_CHECK_SUCCESS, null);
    }

    @GetMapping("/{memberId}")
    public ResponseMessage<MemberResponseDto> getMember(@PathVariable Long memberId){
        MemberResponseDto memberResponseDto = memberService.getMember(memberId);
        return new ResponseMessage<>(MEMBER_LOOKUP_SUCCESS, memberResponseDto);
    }

    @PatchMapping("/{memberId}")
    public ResponseMessage<?> updateMember(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody UpdateMemberRequestDto updateMemberRequestDto,
                                            @PathVariable Long memberId) {
        memberService.updateMember(userDetails.getMember(), updateMemberRequestDto, memberId);
        return new ResponseMessage<>(MEMBER_UPDATE_SUCCESS, null);
    }
}