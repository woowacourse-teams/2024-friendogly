package com.woowacourse.friendogly.auth;

import com.woowacourse.friendogly.auth.service.jwt.JwtProvider;
import com.woowacourse.friendogly.exception.FriendoglyException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class OAuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    public OAuthArgumentResolver(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(OAuth.class);
    }

    @Override
    public Long resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isBlank(accessToken)) {
            throw new FriendoglyException("토큰 정보가 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        return Long.parseLong(jwtProvider.validateAndExtract(accessToken));
    }
}
