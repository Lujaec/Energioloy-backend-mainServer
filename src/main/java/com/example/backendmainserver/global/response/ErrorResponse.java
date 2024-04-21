package com.example.backendmainserver.global.response;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class ErrorResponse {
    private final int status;
    private final String message;

    public static ErrorResponse create() {
        return new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR.getStatus().value(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .build();
    }
}
