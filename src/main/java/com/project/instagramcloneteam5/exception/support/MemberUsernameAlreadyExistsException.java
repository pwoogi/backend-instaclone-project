package com.project.instagramcloneteam5.exception.support;

public class MemberUsernameAlreadyExistsException extends RuntimeException{

    public MemberUsernameAlreadyExistsException(String message) {
        super(message);
    }
}
