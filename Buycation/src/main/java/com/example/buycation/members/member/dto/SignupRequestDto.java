package com.example.buycation.members.member.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class SignupRequestDto {
    @NotBlank
    @Pattern(regexp = "\\\\w+@\\\\w+\\\\.\\\\w+(\\\\.\\\\w+)?")
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "")
    private String password;

    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z0*9가-힣])[a-z0-9가-힣]{2,10}$", message = "소문자 또는 대문자 또는 한글 포함 2~10자리입니다.")
    private String nickname;
    private String address;
}
