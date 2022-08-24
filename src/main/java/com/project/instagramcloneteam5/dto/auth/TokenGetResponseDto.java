package com.project.instagramcloneteam5.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenGetResponseDto {
    private String accessToken;
    private String refreshToken;
    private String username;

}
