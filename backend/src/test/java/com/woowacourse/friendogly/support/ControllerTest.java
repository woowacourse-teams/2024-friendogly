package com.woowacourse.friendogly.support;

import com.woowacourse.friendogly.auth.service.jwt.JwtProvider;
import com.woowacourse.friendogly.auth.service.jwt.TokenPayload;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@DirtiesContext
public abstract class ControllerTest {

    @Autowired
    private JwtProvider jwtProvider;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    protected String getMemberAccessToken(Long memberId) {
        return jwtProvider.generateTokens(new TokenPayload(memberId)).accessToken();
    }
}
