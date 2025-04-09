package com.happy.friendogly.utils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.happy.friendogly.auth.AuthArgumentResolver;
import com.happy.friendogly.playground.dto.request.SavePlaygroundRequest;
import com.happy.friendogly.utils.logging.MdcLoggingInterceptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class MdcLoggingTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MdcLoggingInterceptor mdcLoggingInterceptor;

    @MockBean
    private AuthArgumentResolver authArgumentResolver;

    // TODO: Interceptor, ArgumentResolver 동작 여부가 아닌, MDC.put(uri), MDC.put(memberId), MDC.clear() 호출 순서 검증하기
    /**
     * 1) 사용자 요청
     * 2) Interceptor MDC.put(uri)
     * 3) ArgumentResolver JWT 파싱 및 MDC.put(memberId)
     * 4) Controller, Service 동작
     * 5) Interceptor MDC.clear()
     */
    @DisplayName("요청 처리 중 Interceptor와 ArgumentResolver가 일련의 순서대로 동작한다.")
    @Test
    void mdcInOrder() throws Exception {
        // given
        willReturn(true).given(mdcLoggingInterceptor).preHandle(any(), any(), any());
        willReturn(true).given(authArgumentResolver).supportsParameter(any());
        willReturn(1L).given(authArgumentResolver).resolveArgument(any(), any(), any(), any());

        // when
        mockMvc.perform(get("/playgrounds/locations"));

        // then
        InOrder order = inOrder(mdcLoggingInterceptor, authArgumentResolver);
        order.verify(mdcLoggingInterceptor).preHandle(any(), any(), any());
        order.verify(authArgumentResolver).resolveArgument(any(), any(), any(), any());
        order.verify(mdcLoggingInterceptor).afterCompletion(any(), any(), any(), any());
    }

    @DisplayName("요청 처리 도중 예외가 발생해도 MDC.clear() 호출이 보장된다.")
    @Test
    void mdcClear() throws Exception {
        // given
        willReturn(true).given(mdcLoggingInterceptor).preHandle(any(), any(), any());
        willReturn(true).given(authArgumentResolver).supportsParameter(any());
        willReturn(1L).given(authArgumentResolver).resolveArgument(any(), any(), any(), any());

        ObjectMapper objectMapper = new ObjectMapper();
        SavePlaygroundRequest invalidRequest = new SavePlaygroundRequest(999, 999);
        String invalidContent = objectMapper.writeValueAsString(invalidRequest);

        // when
        mockMvc.perform(post("/playgrounds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidContent));

        // then
        verify(mdcLoggingInterceptor).afterCompletion(any(), any(), any(), any());
    }
}
