package com.example.buycation.mail.mapper;

import com.example.buycation.mail.entity.EmailCheck;
import org.springframework.stereotype.Component;

@Component
public class EmailCheckMapper {

    public EmailCheck toEmailCheck(String email, String code, boolean status, long expiredTime) {
        return EmailCheck.builder()
                .code(code)
                .email(email)
                .status(status)
                .expiredTime(expiredTime)
                .build();
    }
}
