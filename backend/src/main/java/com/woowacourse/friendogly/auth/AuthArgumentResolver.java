package com.woowacourse.friendogly.auth;

import com.woowacourse.friendogly.exception.FriendoglyException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Long resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        // TODO: token에서 추출하도록 변경
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String authorizationValue = request.getHeader(HttpHeaders.AUTHORIZATION);

        System.out.println("==========================");
        System.out.println(authorizationValue);
        System.out.println("==========================");

        if (authorizationValue == null) {
            throw new FriendoglyException("로그인 후에 사용할 수 있습니다.");
        }

        return Long.parseLong(authorizationValue);
    }
}
