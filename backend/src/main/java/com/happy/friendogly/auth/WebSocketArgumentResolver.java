package com.happy.friendogly.auth;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import com.happy.friendogly.auth.service.jwt.JwtProvider;
import com.happy.friendogly.exception.FriendoglyException;
import io.micrometer.common.util.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    public WebSocketArgumentResolver(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(WebSocketAuth.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String accessToken = accessor.getFirstNativeHeader(AUTHORIZATION);

        if (StringUtils.isBlank(accessToken)) {
            throw new FriendoglyException("토큰 정보가 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        return Long.parseLong(jwtProvider.validateAndExtract(accessToken));
    }
}
