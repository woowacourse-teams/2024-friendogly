package com.woowacourse.friendogly.member.controller;

import com.woowacourse.friendogly.auth.domain.KakaoMember;
import com.woowacourse.friendogly.auth.repository.KakaoMemberRepository;
import com.woowacourse.friendogly.member.dto.request.SaveMemberRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
class MemberControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Autowired
    private KakaoMemberRepository kakaoMemberRepository;

    @DisplayName("정상적으로 회원을 생성하면 201을 반환한다.")
    @Test
    void saveMember() {
        KakaoMember savedKakaoMember = kakaoMemberRepository.save(new KakaoMember(1L));
        SaveMemberRequest request = new SaveMemberRequest("땡이", "member@email.com");

        // ArgumentResolver가 테스트에서 동작하지 않아 param으로 직접 값을 넣어줌
        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .header(HttpHeaders.AUTHORIZATION, "accessToken")
                .param("kakaoMemberId", savedKakaoMember.getKakaoMemberId())
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("닉네임 길이가 15자를 초과하는 경우 400을 반환한다.")
    @Test
    void saveMember_Fail_NameLengthOver() {
        SaveMemberRequest request = new SaveMemberRequest("1234567890123456", "member@email.com");
        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("이메일 형식이 아닌 경우 400을 반환한다.")
    @Test
    void saveMember_Fail_InvalidEmailFormat() {
        SaveMemberRequest request = new SaveMemberRequest("땡이", "이메일 형식이 아닌 문자열");
        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
