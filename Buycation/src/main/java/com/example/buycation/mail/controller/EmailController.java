package com.example.buycation.mail.controller;

import com.example.buycation.common.ResponseMessage;
import com.example.buycation.mail.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.buycation.common.MessageCode.EMAIL_CONFIRM_CODE_CHECK_SUCCESS;
import static com.example.buycation.common.MessageCode.EMAIL_CONFIRM_CODE_SUCCESS;

@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @GetMapping("/api/members/signup/email")
    public ResponseMessage<?> emailConfirm(@RequestParam String email) throws Exception {
        return new ResponseMessage<>(EMAIL_CONFIRM_CODE_SUCCESS, emailService.sendSimpleMessage(email));
    }

    @PutMapping("/api/members/signup/emailcheck")
    public ResponseMessage<?> emailCheck(@RequestParam String email,
                                         @RequestParam String code) {
        emailService.emailCheck(email, code);
        return new ResponseMessage<>(EMAIL_CONFIRM_CODE_CHECK_SUCCESS, null);
    }
}
