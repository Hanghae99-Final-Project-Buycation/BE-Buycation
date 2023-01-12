package com.example.buycation.members.member.controller;

import com.example.buycation.common.MessageCode;
import com.example.buycation.common.ResponseMessage;
import com.example.buycation.members.member.dto.LoginRequestDto;
import com.example.buycation.members.member.dto.SignupRequestDto;
import com.example.buycation.members.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static com.example.buycation.common.MessageCode.*;

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
}