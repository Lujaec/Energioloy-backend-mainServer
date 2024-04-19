package com.example.backendmainserver.global.response;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
@Builder
public class ErrorResponse {
    private  int status;
    private  String message;

}
