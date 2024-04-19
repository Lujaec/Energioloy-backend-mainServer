package com.example.backendmainserver.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {
    /**
     * 200 OK
     */
    OK(HttpStatus.OK, "요청이 성공했습니다."),

    /**
     * 201 CREATED SUCCESS
     */
    CREATED(HttpStatus.CREATED, "생성 요청이 성공했습니다."),
    USER_CREATED(HttpStatus.CREATED, "유저 회원가입이 성공했습니다."),
    TEAM_CREATED(HttpStatus.CREATED, "팀 등록에 성공했습니다.");

    private final HttpStatus status;
    private final String message;

    public int getStatusCode() {
        return status.value();
    }
}