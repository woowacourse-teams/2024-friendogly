package com.woowacourse.friendogly.club.controller;

import com.woowacourse.friendogly.club.dto.request.SaveClubRequest;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.member.dto.request.SaveMemberRequest;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.SizeType;
import com.woowacourse.friendogly.pet.dto.request.SavePetRequest;
import com.woowacourse.friendogly.support.ControllerTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class ClubControllerTest extends ControllerTest {

    //TODO : 인증 인가 구현 후 컨트롤러 테스트 작성
    @DisplayName("모임을 생성하면 200을 응답한다.")
    @Test
    void save() {
        SaveMemberRequest saveMemberRequest = new SaveMemberRequest("땡이", "member@email.com");
        Member member = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(saveMemberRequest)
                .when().post("/members")
                .then().log().all()
                .extract()
                .as(Member.class);

        SavePetRequest savePetRequest = new SavePetRequest(
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                "SMALL",
                "FEMALE_NEUTERED",
                "http://www.google.com"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member.getId())
                .body(savePetRequest)
                .when().post("/pets")
                .then().log().all();

        SaveClubRequest request = new SaveClubRequest(
                "모임 제목",
                "모임 내용",
                "https://clubImage.com",
                "서울특별시 송파구 신정동 잠실 5동",
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL),
                5,
                List.of(1L)
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member.getId())
                .body(request)
                .when().post("/clubs")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
}
