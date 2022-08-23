package com.project.instagramcloneteam5.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_NULL) //  null 값을 가지는 필드는, JSON 응답에 포함되지 않음
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Response {
    private boolean success;
    private int code;
    private Result result;

    public static Response success() {
        return new Response(true, 200, null);
    }

    public static <T> Response success(T data) { // 5
        return new Response(true, 200, new Success<>(data));
    }

    public static Response failure(int code, String msg) { // 6
        return new Response(false, code, new Failure(msg));
    }



    public static Response success(int code, String msg) {
        return new Response(true, code, new Success<>(msg));
    }
}