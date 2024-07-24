package com.woowacourse.friendogly.club.controller;

import com.woowacourse.friendogly.club.dto.request.SaveClubMemberRequest;
import com.woowacourse.friendogly.club.dto.request.SaveClubRequest;
import com.woowacourse.friendogly.club.dto.response.SaveClubResponse;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.dto.request.SaveMemberRequest;
import com.woowacourse.friendogly.member.dto.response.SaveMemberResponse;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.SizeType;
import com.woowacourse.friendogly.pet.dto.request.SavePetRequest;
import com.woowacourse.friendogly.pet.dto.response.SavePetResponse;
import com.woowacourse.friendogly.support.ControllerTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class ClubControllerTest extends ControllerTest {

    private SaveMemberResponse member;

    private SavePetResponse pet;

    @BeforeEach
    void setMemberAndPet() {
        SaveMemberRequest saveMemberRequest = new SaveMemberRequest("정지호", "member@email.com");
        member = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(saveMemberRequest)
                .when().post("/members")
                .then().log().all()
                .extract()
                .as(SaveMemberResponse.class);

        SavePetRequest savePetRequest = new SavePetRequest(
                "땡이",
                "귀여운 땡이입니다.",
                LocalDate.now().minusDays(1L),
                "SMALL",
                "FEMALE_NEUTERED",
                "http://www.google.com"
        );

        pet = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member.id())
                .body(savePetRequest)
                .when().post("/pets")
                .then().log().all()
                .extract()
                .as(SavePetResponse.class);
    }

    //TODO : 인증 인가 구현 후 컨트롤러 테스트 작성
    @DisplayName("모임을 생성하면 200을 응답한다.")
    @Test
    void save() {
        SaveClubRequest request = new SaveClubRequest(
                "모임 제목",
                "모임 내용",
                "https://clubImage.com",
                "서울특별시 송파구 신정동 잠실 5동",
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL),
                5,
                List.of(pet.id())
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member.id())
                .body(request)
                .when().post("/clubs")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("모임에 참여하면, 201을 응답한다.")
    @Test
    void saveClubMember_201() {
        //모임 생성
        SaveClubRequest request = new SaveClubRequest(
                "모임 제목",
                "모임 내용",
                "https://clubImage.com",
                "서울특별시 송파구 신정동 잠실 5동",
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED, Gender.MALE_NEUTERED),
                Set.of(SizeType.SMALL),
                5,
                List.of(pet.id())
        );

        SaveClubResponse club = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member.id())
                .body(request)
                .when().post("/clubs")
                .then().log().all()
                .extract()
                .as(SaveClubResponse.class);

        //새로운 회원과 팻 생성.
        SaveMemberRequest saveMemberRequest = new SaveMemberRequest("이건상", "member1@email.com");
        Member newMember = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(saveMemberRequest)
                .when().post("/members")
                .then().log().all()
                .extract()
                .as(Member.class);

        SavePetRequest savePetRequest = new SavePetRequest(
                "욜뀰",
                "사실 고양이 입니다.",
                LocalDate.now().minusDays(1L),
                "SMALL",
                "MALE_NEUTERED",
                "http://www.google123.com"
        );
        SavePetResponse newMemberPet = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, newMember.getId())
                .body(savePetRequest)
                .when().post("/pets")
                .then().log().all()
                .extract()
                .as(SavePetResponse.class);

        //when,then
        SaveClubMemberRequest saveClubMemberRequest = new SaveClubMemberRequest(List.of(newMemberPet.id()));
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, newMember.getId())
                .body(saveClubMemberRequest)
                .when().post("/clubs/" + club.id() + "/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("모임에 참여 중 예외가 발생하면, 400을 응답한다.")
    @Test
    void saveClubMember_400() {
        //모임 생성
        SaveClubRequest request = new SaveClubRequest(
                "모임 제목",
                "모임 내용",
                "https://clubImage.com",
                "서울특별시 송파구 신정동 잠실 5동",
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED, Gender.MALE_NEUTERED),
                Set.of(SizeType.SMALL),
                5,
                List.of(pet.id())
        );

        SaveClubResponse club = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member.id())
                .body(request)
                .when().post("/clubs")
                .then().log().all()
                .extract()
                .as(SaveClubResponse.class);

        //이미 가입한 회원이 또 참여 신청
        SaveClubMemberRequest saveClubMemberRequest = new SaveClubMemberRequest(List.of(pet.id()));
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member.id())
                .body(saveClubMemberRequest)
                .when().post("/clubs/" + club.id() + "/members")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST
                        .value());
    }
}
