package com.example.buycation.members.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateMemberRequestDto {
    private String nickname;
    private String profileImage;
    private String address;
}
