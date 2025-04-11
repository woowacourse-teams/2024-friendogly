package com.happy.friendogly.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.happy.friendogly.auth.AuthArgumentResolver;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class MdcLoggingTest {

    private static final String PUT_URI = "put-uri";
    private static final String PUT_MEMBER_ID = "put-memberId";
    private static final String CLEAR = "clear";
    
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private AuthArgumentResolver authArgumentResolver;

    /**
     * 1) 사용자 요청
     * 2) Interceptor MDC.put(uri)
     * 3) ArgumentResolver JWT 파싱 및 MDC.put(memberId)
     * 4) Controller, Service 동작
     * 5) Interceptor MDC.clear()
     */
    @DisplayName("요청 처리 중 Interceptor와 ArgumentResolver가 일련의 순서대로 동작한다.")
    @Test
    void mdcClearStatic() throws Exception {
        try (MockedStatic<MDC> mdcMock = mockStatic(MDC.class)) {
            List<String> actualOrder = new ArrayList<>();
            List<String> expectedOrder = List.of(PUT_URI, PUT_MEMBER_ID, CLEAR);

            // given
            mdcMock.when(() -> MDC.put("uri", "/playgrounds/locations"))
                    .then(invocation -> actualOrder.add(PUT_URI));

            mdcMock.when(() -> MDC.put("memberId", "1"))
                    .then(invocation -> actualOrder.add(PUT_MEMBER_ID));

            mdcMock.when(MDC::clear)
                    .then(invocation -> actualOrder.add(CLEAR));

            willAnswer(invocation -> {
                MDC.put("memberId", "1");
                return 1L;
            }).given(authArgumentResolver).resolveArgument(any(), any(), any(), any());

            // when
            mockMvc.perform(get("/playgrounds/locations"));

            // then
            assertThat(actualOrder).containsExactlyInAnyOrderElementsOf(expectedOrder);
        }
    }

    @DisplayName("요청 처리 도중 예외가 발생해도 MDC.clear() 호출이 보장된다.")
    @Test
    void mdcClear() throws Exception {
        try (MockedStatic<MDC> mdcMock = mockStatic(MDC.class)) {
            // when: Authorization 헤더가 없어 처리 도중 예외 발생
            mockMvc.perform(get("/playgrounds/locations"));

            // then
            mdcMock.verify(MDC::clear);
        }
    }
}
