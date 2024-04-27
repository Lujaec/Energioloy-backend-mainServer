package com.example.backendmainserver.global.response;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class ErrorResponse {
    private final int status;
    private final String message;

    /**
     * 디폴트 에러 메세지 - 500
     * @return
     */
    public static ErrorResponse create() {
        return ErrorResponse.builder()
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getStatusCode())
                .message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage())
                .build();
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getStatusCode())
                .message(errorCode.getMessage())
                .build();
    }
}
