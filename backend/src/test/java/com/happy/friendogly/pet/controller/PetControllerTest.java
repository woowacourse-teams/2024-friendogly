package com.happy.friendogly.pet.controller;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.pet.dto.request.SavePetRequest;
import com.happy.friendogly.support.ControllerTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.io.File;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
class PetControllerTest extends ControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        member = memberRepository.save(Member.builder()
                .name("견주")
                .build());
    }

    @DisplayName("정상적으로 반려견을 생성하면 201을 반환한다.")
    @Test
    void savePet() {
        SavePetRequest request = new SavePetRequest(
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                "SMALL",
                "FEMALE_NEUTERED",
                "http://www.google.com"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .header(HttpHeaders.AUTHORIZATION, getMemberAccessToken(member.getId()))
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("닉네임 길이가 8자를 초과하는 경우 400을 반환한다.")
    @Test
    void savePet_Fail_NameLengthOver() {
        SavePetRequest request = new SavePetRequest(
                "123456789",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                "SMALL",
                "FEMALE_NEUTERED",
                "http://www.google.com"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .header(HttpHeaders.AUTHORIZATION, getMemberAccessToken(member.getId()))
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("한 줄 설명의 길이가 20자를 초과하는 경우 400을 반환한다.")
    @Test
    void savePet_Fail_DescriptionLengthOver() {
        SavePetRequest request = new SavePetRequest(
                "땡이",
                "123456789012345678901",
                LocalDate.now().minusDays(1L),
                "SMALL",
                "FEMALE_NEUTERED",
                "http://www.google.com"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .header(HttpHeaders.AUTHORIZATION, getMemberAccessToken(member.getId()))
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("생년월일로 미래의 날짜를 입력하는 경우 400을 반환한다.")
    @Test
    void savePet_Fail_BirthDateFuture() {
        SavePetRequest request = new SavePetRequest(
                "땡이",
                "땡이입니다.",
                LocalDate.now().plusDays(1L),
                "SMALL",
                "FEMALE_NEUTERED",
                "http://www.google.com"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .header(HttpHeaders.AUTHORIZATION, getMemberAccessToken(member.getId()))
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("이미지 URL이 형식에 맞지 않는 경우 400을 반환한다.")
    @Test
    void savePet_Fail_InvalidUrlFormat() {
        SavePetRequest request = new SavePetRequest(
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                "SMALL",
                "FEMALE_NEUTERED",
                "내맘대로 URL주소 지정하기~"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .header(HttpHeaders.AUTHORIZATION, getMemberAccessToken(member.getId()))
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("한 회원에 대해 5마리를 초과하는 수의 강아지를 등록하는 경우 400을 반환한다.")
    @Test
    void savePet_Fail_OverPetCapacity() {
        SavePetRequest request = new SavePetRequest(
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                "SMALL",
                "FEMALE_NEUTERED",
                "http://www.google.com"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .header(HttpHeaders.AUTHORIZATION, getMemberAccessToken(member.getId()))
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .header(HttpHeaders.AUTHORIZATION, getMemberAccessToken(member.getId()))
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .header(HttpHeaders.AUTHORIZATION, getMemberAccessToken(member.getId()))
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .header(HttpHeaders.AUTHORIZATION, getMemberAccessToken(member.getId()))
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .header(HttpHeaders.AUTHORIZATION, getMemberAccessToken(member.getId()))
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .contentType(ContentType.MULTIPART)
                .header(HttpHeaders.AUTHORIZATION, getMemberAccessToken(member.getId()))
                .multiPart("image", new File("./src/test/resources/real_ddang.jpg"))
                .multiPart("request", request, "application/json")
                .when().post("/pets")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
