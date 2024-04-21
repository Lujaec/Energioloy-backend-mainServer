package com.example.backendmainserver.global.exception;

import com.example.backendmainserver.global.response.ErrorCode;
import lombok.Getter;

@Getter
public class DuplicateUserException extends GlobalException {

    public DuplicateUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
