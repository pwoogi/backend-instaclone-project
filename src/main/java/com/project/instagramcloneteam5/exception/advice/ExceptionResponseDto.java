package com.project.instagramcloneteam5.exception.advice;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.transform.Result;

@Getter
@Setter
@NoArgsConstructor
public class ExceptionResponseDto {
    private String code;
    private String msg;
    private Object data;

    public ExceptionResponseDto(Code code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    public ExceptionResponseDto(Code code, Object data){
        this.code = code.getCode();
        this.msg = code.getMsg();
        this.data = data;
    }
}