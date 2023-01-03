package com.example.buycation.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MessageCode {

    SUCCESS("성공",200);

    private final String msg;
    private final int statusCode;
}
