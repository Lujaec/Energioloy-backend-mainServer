package com.example.backendmainserver.auth.presentation;


import com.example.backendmainserver.global.exception.GlobalException;
import com.example.backendmainserver.global.response.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;

import java.util.Objects;

public class AuthorizationExtractor {
    private static final String BEARER_TYPE = "Bearer ";

    public static String extract(final HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (Objects.isNull(authorizationHeader)) {
            throw new GlobalException(ErrorCode.EXPIRED_JWT_ACCESS_TOKEN);
        }

        validateAuthorizationFormat(authorizationHeader);
        return authorizationHeader.substring(BEARER_TYPE.length()).trim();
    }

    private static void validateAuthorizationFormat(final String authorizationHeader) {
        if (!authorizationHeader.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            throw new GlobalException(ErrorCode.INVALID_JWT_ACCESS_TOKEN);
        }
    }
}
