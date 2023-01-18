package com.example.buycation.members.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoMemberInfoDto {
    private Long id;
    private String email;
    private String nickname;
}