package com.happy.friendogly;

import com.happy.friendogly.common.ApiResponse;
import com.happy.friendogly.common.ErrorCode;
import com.happy.friendogly.common.ErrorResponse;
import com.happy.friendogly.exception.FriendoglyException;
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
        //TODO: 커스텀 예외 내부에 우리 잘못인지 구분하는 로직 만들기 (일단은 외부api가 안되면 5xx, 사용자잘못이면 4xx)
        if (exception.getHttpStatus().is5xxServerError()) {
            log.error(exception.getMessage(), exception);
        }
        if (exception.getHttpStatus().is4xxClientError()) {
            log.warn(exception.getMessage(), exception);
        }

        ErrorResponse errorResponse = new ErrorResponse(
                exception.getErrorCode(),
                exception.getMessage(),
                Collections.emptyList()
        );

        return new ResponseEntity<>(ApiResponse.ofError(errorResponse), exception.getHttpStatus());
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

        return new ResponseEntity<>(ApiResponse.ofError(errorResponse), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handle(HttpMessageNotReadableException exception) {
        log.warn(exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.DEFAULT_ERROR_CODE,
                "읽을 수 없는 HTTP 메세지입니다.",
                Collections.emptyList()
        );

        return new ResponseEntity<>(ApiResponse.ofError(errorResponse), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handle(HttpRequestMethodNotSupportedException exception) {
        log.warn(exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.DEFAULT_ERROR_CODE,
                "지원하지 않는 HTTP 메서드입니다.",
                Collections.emptyList()
        );

        return new ResponseEntity<>(ApiResponse.ofError(errorResponse), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handle(NoResourceFoundException exception) {
        log.warn(exception.getMessage());

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.DEFAULT_ERROR_CODE,
                "존재하지 않는 요청 경로입니다.",
                Collections.emptyList()
        );

        return new ResponseEntity<>(ApiResponse.ofError(errorResponse), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handle(Exception exception) {
        log.error(exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.UNKNOWN_EXCEPTION,
                "알 수 없는 예외가 발생했습니다.",
                Collections.emptyList()
        );

        return new ResponseEntity<>(ApiResponse.ofError(errorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
