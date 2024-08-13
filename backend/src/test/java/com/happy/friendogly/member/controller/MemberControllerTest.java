package com.happy.friendogly.member.controller;

import static org.mockito.Mockito.when;

import com.happy.friendogly.auth.dto.KakaoUserResponse;
import com.happy.friendogly.auth.service.KakaoOauthClient;
import com.happy.friendogly.auth.service.jwt.JwtProperties;
import com.happy.friendogly.auth.service.jwt.JwtProvider;
import com.happy.friendogly.member.dto.request.SaveMemberRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
class MemberControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private JwtProvider jwtProvider;

    @MockBean
    private KakaoOauthClient kakaoOauthClient;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @DisplayName("정상적으로 회원을 생성하면 201을 반환한다.")
    @Test
    void saveMember() {
        String kakaoAccessToken = "kakao";
        SaveMemberRequest request = new SaveMemberRequest("땡이", kakaoAccessToken);

        when(kakaoOauthClient.getUserInfo(kakaoAccessToken))
                .thenReturn(new KakaoUserResponse("kakaoMemberId"));

        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("닉네임 길이가 15자를 초과하는 경우 400을 반환한다.")
    @Test
    void saveMember_Fail_NameLengthOver() {
        SaveMemberRequest request = new SaveMemberRequest("1234567890123456",  "code");
        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
