package com.example.backendmainserver.domain.user.application.dto.response;

import lombok.Builder;

@Builder
public record JoinResponse(String loginId) {
}
