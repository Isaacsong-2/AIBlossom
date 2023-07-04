package com.aiblossom.common.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST) // response로 들어가는 에러 코드를 400으로 통일
public class HanghaeBlogException extends RuntimeException {

    private final HanghaeBlogErrorCode errorCode;

    public HanghaeBlogException(HanghaeBlogErrorCode errorCode, Throwable cause) {
        super(errorCode.getErrorMessage(), cause, false, false);
        this.errorCode = errorCode;
    }

    public HanghaeBlogErrorCode getErrorCode() {
        return this.errorCode;
    }
}