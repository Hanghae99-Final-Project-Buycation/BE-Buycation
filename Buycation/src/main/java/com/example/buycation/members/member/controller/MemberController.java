package com.example.buycation.members.member.controller;

import com.example.buycation.common.ResponseMessage;
import com.example.buycation.members.member.dto.LoginRequestDto;
import com.example.buycation.members.member.dto.SignupRequestDto;
import com.example.buycation.members.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseMessage<?> signup(@RequestBody SignupRequestDto signupRequestDto) {
        memberService.signup(signupRequestDto);
        return new ResponseMessage<>("회원가입이 완료되었습니다.", 200, null);
    }

    @PostMapping("/login")
    public ResponseMessage<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        memberService.login(loginRequestDto, response);
        return new ResponseMessage<>("로그인에 성공했습니다.", 200, null);
    }
}