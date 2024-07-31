package com.woowacourse.friendogly.support;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
public abstract class ControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

}
