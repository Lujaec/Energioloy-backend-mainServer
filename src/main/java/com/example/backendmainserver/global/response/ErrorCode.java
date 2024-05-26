package com.example.backendmainserver.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    /**
     *  400 Bad Request
     */


    /**
     *  401 Unauthorized
     */
    UNAUTHORIZED(401,HttpStatus.UNAUTHORIZED, "접근할 수 있는 권한이 없습니다. access token을 확인하세요."),
    INVALID_JWT_ACCESS_TOKEN(401,HttpStatus.UNAUTHORIZED,  "유효하지 않은 ACCESS TOKEN 입니다"),
    EXPIRED_JWT_ACCESS_TOKEN(401,HttpStatus.UNAUTHORIZED, "ACCESS TOKEN이 만료되었습니다. 재발급 받아주세요."),
    INVALID_JWT_REFRESH_TOKEN(401,HttpStatus.UNAUTHORIZED,  "유효하지 않은 REFRESH TOKEN 입니다."),
    EXPIRED_JWT_REFRESH_TOKEN(401,HttpStatus.UNAUTHORIZED, "REFRESH TOKEN이 만료되었습니다. 다시 로그인해주세요"),


    /**
     *  403 Forbidden
     */
    WRONG_USER_ACCESS(403,HttpStatus.FORBIDDEN, "잘못된 접근 권한의 유저입니다."),
    WRONG_USER_PASSWORD(403,HttpStatus.FORBIDDEN, "입력하신 비밀번호가 올바르지 않습니다."),
    USER_NOT_HAVE_ROOM(403,HttpStatus.FORBIDDEN, "해당 사용자는 지정한 룸에 대한 권한이 없습니다."),


    /**
     *  404 Not Found
     */
    FCM_TOKEN_NOT_FOUND(404,HttpStatus.NOT_FOUND, "fcm토큰이 올바르지 않습니다."),

    ENTITY_NOT_FOUND(404,HttpStatus.NOT_FOUND, "요청하신 엔티티를 찾을 수 없습니다."),
    USER_NOT_FOUND(404,HttpStatus.NOT_FOUND, "요청하신 유저를 찾을 수 없습니다."),
    NOT_SIGN_IN_KAKAO_ID(404,HttpStatus.NOT_FOUND, "회원가입되지 않은 KAKAO 계정입니다. 회원가입을 진행해 주세요"),

    ENUM_NOT_FOUND(404,HttpStatus.NOT_FOUND, "요청하신 ENUM TYPE을 찾을 수 없습니다."),
    ROOM_NOT_FOUND(404,HttpStatus.NOT_FOUND, "요청하신 ROOM을 찾을 수 없습니다."),


    /**
     * 409 Conflict
     */
    DUPLICATE_SAMPLE_TEXT(409, HttpStatus.CONFLICT, "이미 존재하는 TEXT입니다."),
    DUPLICATE_USER_NAME(409, HttpStatus.CONFLICT, "이미 존재하는 유저 이름 입니다."),
    DUPLICATE_KAKAO_ID(409, HttpStatus.CONFLICT, "이미 회원가입 된 카카오 계정 입니다."),

    /**
     *  500 INTERNAL SERVER ERROR
     */
    INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    FILE_UPLOAD_FAILED(500, HttpStatus.INTERNAL_SERVER_ERROR, "S3 이미지 업로드에 실패");


    private final int statusCode;
    private final   HttpStatus status;
    private final   String message;
}