package com.example.buycation.mail.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


@Getter
@NoArgsConstructor
@RedisHash(value = "emailCheck")
public class EmailCheck {
    @Id
    private String email;

    private String code;

    private boolean status;

    @TimeToLive
    private Long expiredTime;

    @Builder
    public EmailCheck(String code, String email, boolean status, long expiredTime){
        this.code=code;
        this.email=email;
        this.status=status;
        this.expiredTime=expiredTime;
    }
}
