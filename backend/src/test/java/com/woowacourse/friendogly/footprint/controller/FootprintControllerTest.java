package com.woowacourse.friendogly.footprint.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import com.woowacourse.friendogly.footprint.domain.Footprint;
import com.woowacourse.friendogly.footprint.domain.Location;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.repository.FootprintRepository;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.repository.MemberRepository;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import com.woowacourse.friendogly.pet.repository.PetRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

// TODO: 예외 테스트 추가
// TODO: 공통 응답 포맷에 맞게 변경

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
class FootprintControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private FootprintRepository footprintRepository;

    private Member member1;
    private Member member2;
    private Member member3;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        member1 = memberRepository.save(
                Member.builder()
                        .name("트레")
                        .tag("123123")
                        .email("test@test.com")
                        .build()
        );

        member2 = memberRepository.save(
                Member.builder()
                        .name("트레")
                        .tag("123123")
                        .email("test@test.com")
                        .build()
        );

        member3 = memberRepository.save(
                Member.builder()
                        .name("트레")
                        .tag("123123")
                        .email("test@test.com")
                        .build()
        );

        petRepository.save(
                Pet.builder()
                        .member(member1)
                        .name("멍멍이1")
                        .description("멍멍이1입니다")
                        .birthDate(LocalDate.now().minusYears(1))
                        .sizeType(SizeType.SMALL)
                        .gender(Gender.MALE_NEUTERED)
                        .imageUrl("https://picsum.photos/200")
                        .build()
        );

        petRepository.save(
                Pet.builder()
                        .member(member2)
                        .name("멍멍이2")
                        .description("멍멍이2입니다")
                        .birthDate(LocalDate.now().minusYears(2))
                        .sizeType(SizeType.MEDIUM)
                        .gender(Gender.FEMALE_NEUTERED)
                        .imageUrl("https://picsum.photos/200")
                        .build()
        );

        petRepository.save(
                Pet.builder()
                        .member(member3)
                        .name("멍멍이3")
                        .description("멍멍이3입니다")
                        .birthDate(LocalDate.now().minusYears(3))
                        .sizeType(SizeType.LARGE)
                        .gender(Gender.FEMALE)
                        .imageUrl("https://picsum.photos/200")
                        .build()
        );
    }

    @DisplayName("발자국을 정상적으로 생성한다. (201)")
    @Test
    void save() {
        float latitude = 37.0F;
        float longitude = 127.0F;

        SaveFootprintRequest request = new SaveFootprintRequest(latitude, longitude);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member1.getId())
                .body(request)
                .when().post("/footprints")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("latitude", is(latitude))
                .body("longitude", is(longitude));
    }

    @DisplayName("발자국 ID로 발자국의 정보를 조회할 수 있다. (200)")
    @Test
    void findOne() {
        Footprint footprint = footprintRepository.save(
                Footprint.builder()
                        .member(member1)
                        .location(new Location(37.0, 127.0))
                        .build()
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member1.getId())
                .pathParam("footprintId", footprint.getId())
                .when().get("/footprints/{footprintId}")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("위도, 경도로 주변 발자국을 조회할 수 있다. (200)")
    @Test
    void findNear() {
        footprintRepository.save(
                Footprint.builder()
                        .member(member2)
                        .location(new Location(37.51365, 127.09831))
                        .build()
        );

        footprintRepository.save(
                Footprint.builder()
                        .member(member3)
                        .location(new Location(37.51314, 127.10425))
                        .build()
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member1.getId())
                .queryParam("latitude", 37.5171728)
                .queryParam("longitude", 127.1047797)
                .when().get("/footprints/near")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body("latitude", contains(37.51365f, 37.51314f))
                .body("longitude", contains(127.09831f, 127.10425f));
    }

    @DisplayName("자신이 마지막으로 생성한 발자국의 시간을 조회할 수 있다.")
    @Test
    void findMyLatestFootprintTime() {
        footprintRepository.save(
                Footprint.builder()
                        .member(member1)
                        .location(new Location(37.0, 127.0))
                        .build()
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member1.getId())
                .when().get("/footprints/mine/latest")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
