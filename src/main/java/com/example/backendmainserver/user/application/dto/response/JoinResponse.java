package com.example.backendmainserver.user.application.dto.response;

import lombok.Builder;

@Builder
public record JoinResponse(String loginId) {
}
