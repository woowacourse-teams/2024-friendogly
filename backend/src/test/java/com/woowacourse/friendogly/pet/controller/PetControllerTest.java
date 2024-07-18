package com.woowacourse.friendogly.pet.controller;

import com.woowacourse.friendogly.member.dto.request.SaveMemberRequest;
import com.woowacourse.friendogly.pet.dto.request.SavePetRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
class PetControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        SaveMemberRequest request = new SaveMemberRequest("견주", "ddang@email.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("정상적으로 반려견을 생성하면 201을 반환한다.")
    @Test
    void savePet() {
        SavePetRequest request = new SavePetRequest(
                1L,
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                "SMALL",
                "FEMALE_NEUTERED",
                "http://www.google.com"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("닉네임 길이가 15자를 초과하는 경우 400을 반환한다.")
    @Test
    void savePet_Fail_NameLengthOver() {
        SavePetRequest request = new SavePetRequest(
                1L,
                "1234567890123456",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                "SMALL",
                "FEMALE_NEUTERED",
                "http://www.google.com"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("한 줄 설명의 길이가 15자를 초과하는 경우 400을 반환한다.")
    @Test
    void savePet_Fail_DescriptionLengthOver() {
        SavePetRequest request = new SavePetRequest(
                1L,
                "땡이",
                "1234567890123456",
                LocalDate.now().minusDays(1L),
                "SMALL",
                "FEMALE_NEUTERED",
                "http://www.google.com"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("생년월일로 미래의 날짜를 입력하는 경우 400을 반환한다.")
    @Test
    void savePet_Fail_BirthDateFuture() {
        SavePetRequest request = new SavePetRequest(
                1L,
                "땡이",
                "땡이입니다.",
                LocalDate.now().plusDays(1L),
                "SMALL",
                "FEMALE_NEUTERED",
                "http://www.google.com"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("이미지 URL이 형식에 맞지 않는 경우 400을 반환한다.")
    @Test
    void savePet_Fail_InvalidUrlFormat() {
        SavePetRequest request = new SavePetRequest(
                1L,
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                "SMALL",
                "FEMALE_NEUTERED",
                "내맘대로 URL주소 지정하기~"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
