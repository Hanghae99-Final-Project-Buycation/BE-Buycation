package com.example.buycation.members.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto {
    private Long memberId;
    private String email;
    private String nickname;
}
