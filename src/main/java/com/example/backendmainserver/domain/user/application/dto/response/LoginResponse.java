package com.example.backendmainserver.domain.user.application.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(Long userId,String nickname,String refreshToken, String accessToken) {
}
