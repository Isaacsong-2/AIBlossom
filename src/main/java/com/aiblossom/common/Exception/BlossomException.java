package com.aiblossom.common.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST) // response로 들어가는 에러 코드를 400으로 통일
public class BlossomException extends RuntimeException {

    private final BlossomErrorCode errorCode;

    public BlossomException(BlossomErrorCode errorCode, Throwable cause) {
        super(errorCode.getErrorMessage(), cause, false, false);
        this.errorCode = errorCode;
    }

    public BlossomErrorCode getErrorCode() {
        return this.errorCode;
    }
}