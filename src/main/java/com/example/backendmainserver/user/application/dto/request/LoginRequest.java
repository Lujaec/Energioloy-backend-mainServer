package com.example.backendmainserver.user.application.dto.request;

import lombok.Builder;

@Builder
public record LoginRequest(String loginId, String password) {
}
