package com.woowacourse.friendogly.exception;

import com.woowacourse.friendogly.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class FriendoglyException extends RuntimeException {

    private ErrorCode errorCode;
    private HttpStatus httpStatus;

    public FriendoglyException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public FriendoglyException(String message, HttpStatus httpStatus) {
        this(message, ErrorCode.DEFAULT_ERROR_CODE, httpStatus);
    }

    public FriendoglyException(String message, ErrorCode errorCode, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
