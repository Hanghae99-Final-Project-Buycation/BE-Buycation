package com.example.buycation.members.member.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String email;
    private String password;
    private String nickname;
    private String address;
}
