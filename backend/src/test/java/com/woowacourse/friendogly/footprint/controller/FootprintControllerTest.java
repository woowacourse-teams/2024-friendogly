package com.woowacourse.friendogly.footprint.controller;

import static com.woowacourse.friendogly.footprint.domain.WalkStatus.AFTER;
import static com.woowacourse.friendogly.footprint.domain.WalkStatus.BEFORE;
import static com.woowacourse.friendogly.footprint.domain.WalkStatus.ONGOING;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

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
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
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

// TODO: DirtiesContext 제거
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

    private Pet pet1;

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

        pet1 = petRepository.save(
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

    @AfterEach
    void setDown() {
        footprintRepository.deleteAll();
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
                .body("data.latitude", is(latitude))
                .body("data.longitude", is(longitude));
    }

    @DisplayName("30초 이내에 발자국을 생성했으면, 발자국을 생성할 수 없다. (400)")
    @Test
    void save_Fail_DueToCooldown() {
        footprintRepository.save(
                Footprint.builder()
                        .member(member1)
                        .location(new Location(37.0, 127.0))
                        .build()
        );

        SaveFootprintRequest request = new SaveFootprintRequest(0.0, 0.0);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member1.getId())
                .body(request)
                .when().post("/footprints")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("isSuccess", is(false));
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
                .statusCode(HttpStatus.OK.value())
                .body("isSuccess", is(true))
                .body("data.memberName", is("트레"))
                .body("data.walkStatus", is("BEFORE"))
                .body("data.pets[0].name", is(pet1.getName().getValue()))
                .body("data.pets[0].description", is(pet1.getDescription().getValue()))
                .body("data.pets[0].birthDate", is(pet1.getBirthDate().getValue().toString()))
                .body("data.pets[0].sizeType", is(pet1.getSizeType().name()))
                .body("data.pets[0].gender", is(pet1.getGender().name()))
                .body("data.pets[0].imageUrl", is(pet1.getImageUrl()))
                .body("data.isMine", is(footprint.isCreatedBy(member1.getId())));
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
                .body("data.size()", is(2))
                .body("data.latitude", contains(37.51365f, 37.51314f))
                .body("data.longitude", contains(127.09831f, 127.10425f));
    }

    @DisplayName("자신이 마지막으로 생성한 발자국의 시간을 조회할 수 있다. (200)")
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
                .statusCode(HttpStatus.OK.value())
                .body("data.createdAt", notNullValue());
    }

    @DisplayName("발자국을 찍은 적이 없으면 마지막 생성 발자국 시간이 null이다. (200)")
    @Test
    void findMyLatestFootprintTime_MyFootprintDoesNotExist() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member1.getId())
                .when().get("/footprints/mine/latest")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("data.changedWalkStatusTime", nullValue());
    }

    @DisplayName("발자국 범위밖에서 안으로 들어오면 산책중으로 상태가 변한다 (200)")
    @Test
    void updateWalkStatus_toOngoing() {
        footprintRepository.save(
                new Footprint(
                        member1,
                        new Location(0,0),
                        BEFORE,
                        null,
                        null,
                        LocalDateTime.now(),
                        false
                        )
        );

        float latitude = 0.0F;
        float longitude = 0.0F;

        SaveFootprintRequest request = new SaveFootprintRequest(latitude, longitude);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member1.getId())
                .body(request)
                .when().patch("/footprints/walk-status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("data.walkStatus", is(ONGOING.toString()));
    }

    @DisplayName("발자국 범위안에서 밖으로 나가면 산책후로 상태가 변한다 (200)")
    @Test
    void updateWalkStatus_toAfter(){
        footprintRepository.save(
                new Footprint(
                        member1,
                        new Location(0,0),
                        ONGOING,
                        LocalDateTime.now(),
                        null,
                        LocalDateTime.now().minusHours(1),
                        false
                )
        );

        float latitude = 37.0F;
        float longitude = 127.0F;

        SaveFootprintRequest request = new SaveFootprintRequest(latitude, longitude);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member1.getId())
                .body(request)
                .when().patch("/footprints/walk-status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("data.walkStatus",is(AFTER.toString()));
    }
}
