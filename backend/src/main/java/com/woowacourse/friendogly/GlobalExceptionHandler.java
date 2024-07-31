package com.woowacourse.friendogly;

import com.woowacourse.friendogly.common.ApiResponse;
import com.woowacourse.friendogly.common.ErrorCode;
import com.woowacourse.friendogly.common.ErrorResponse;
import com.woowacourse.friendogly.exception.FriendoglyException;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

// TODO: 적절한 로그 레벨 설정
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FriendoglyException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handle(FriendoglyException exception) {
        log.warn(exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(
                exception.getErrorCode(),
                exception.getMessage(),
                Collections.emptyList()
        );

        return new ResponseEntity(ApiResponse.ofError(errorResponse), exception.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handle(MethodArgumentNotValidException exception) {
        log.warn(exception.getMessage(), exception);

        List<String> detail = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.DEFAULT_ERROR_CODE,
                "유효하지 않은 요청 값입니다.",
                detail
        );

        return new ResponseEntity(ApiResponse.ofError(errorResponse), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handle(HttpMessageNotReadableException exception) {
        log.warn(exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.DEFAULT_ERROR_CODE,
                "읽을 수 없는 HTTP 메세지입니다.",
                Collections.emptyList()
        );

        return new ResponseEntity(ApiResponse.ofError(errorResponse), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handle(HttpRequestMethodNotSupportedException exception) {
        log.warn(exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.DEFAULT_ERROR_CODE,
                "지원하지 않는 HTTP 메서드입니다.",
                Collections.emptyList()
        );

        return new ResponseEntity(ApiResponse.ofError(errorResponse), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handle(NoResourceFoundException exception) {
        log.warn(exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.DEFAULT_ERROR_CODE,
                "존재하지 않는 요청 경로입니다.",
                Collections.emptyList()
        );

        return new ResponseEntity(ApiResponse.ofError(errorResponse), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception exception) {
        log.error(exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.DEFAULT_ERROR_CODE,
                exception.getMessage(), // TODO: 처리 필요
                Collections.emptyList()
        );

        return new ResponseEntity(ApiResponse.ofError(errorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
