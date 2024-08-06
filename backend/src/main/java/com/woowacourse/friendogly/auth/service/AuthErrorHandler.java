package com.woowacourse.friendogly.auth.service;

import com.woowacourse.friendogly.exception.FriendoglyException;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class AuthErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        throw new FriendoglyException(
                "로그인에 실패했습니다. 다시 시도해주세요.",
                HttpStatus.valueOf(response.getStatusCode().value())
        );
    }
}
