package com.woowacourse.friendogly.auth;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.woowacourse.friendogly.exception.FriendoglyException;
import java.util.List;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(WebSocketAuth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        List<String> values = accessor.getNativeHeader(AUTHORIZATION);
        if (values == null || values.isEmpty()) {
            throw new FriendoglyException("잘못된 헤더 값입니다.", HttpStatus.UNAUTHORIZED);
        }
        return Long.parseLong(values.get(0));
    }
}
