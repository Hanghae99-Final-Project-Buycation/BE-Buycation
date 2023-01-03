package com.example.buycation.common.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {
    private final String result;
    private final String msg;
    private final T data;

    public ResponseDto(String result, String msg, T data){
        this.result = result;
        this.msg = msg;
        this.data = data;
    }
}
