package com.project.instagramcloneteam5.exception.advice;

import lombok.Getter;

@Getter
public class PrivateException extends RuntimeException {
    private Code code;

    public PrivateException(Code code) {
        super(code.getMsg());
        this.code = code;
    }
}