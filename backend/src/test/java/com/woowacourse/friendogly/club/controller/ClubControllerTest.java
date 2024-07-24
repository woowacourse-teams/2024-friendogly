package com.woowacourse.friendogly.club.controller;

import com.woowacourse.friendogly.support.ControllerTest;

//TODO : ApiResponse 에서 DTO 형태로 data 추출 못하는 상태
public class ClubControllerTest extends ControllerTest {
}
/*
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
                .jsonPath().getObject("data", new TypeRef<SaveMemberResponse>() {
                });

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
                .jsonPath().getObject("data", new TypeRef<SavePetResponse>() {
                });

    }

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

        SaveClubResponse club = (SaveClubResponse) RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member.id())
                .body(request)
                .when().post("/clubs")
                .then().log().all()
                .extract()
                .as(ApiResponse.class)
                .data();

        //새로운 회원과 팻 생성.
        SaveMemberRequest saveMemberRequest = new SaveMemberRequest("이건상", "member1@email.com");
        SaveMemberResponse newMember = (SaveMemberResponse) RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(saveMemberRequest)
                .when().post("/members")
                .then().log().all()
                .extract()
                .as(ApiResponse.class)
                .data();

        SavePetRequest savePetRequest = new SavePetRequest(
                "욜뀰",
                "사실 고양이 입니다.",
                LocalDate.now().minusDays(1L),
                "SMALL",
                "MALE_NEUTERED",
                "http://www.google123.com"
        );
        SavePetResponse newMemberPet = (SavePetResponse) RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, newMember.id())
                .body(savePetRequest)
                .when().post("/pets")
                .then().log().all()
                .extract()
                .as(ApiResponse.class)
                .data();

        //when,then
        SaveClubMemberRequest saveClubMemberRequest = new SaveClubMemberRequest(List.of(newMemberPet.id()));
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, newMember.id())
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

        SaveClubResponse club = (SaveClubResponse) RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, member.id())
                .body(request)
                .when().post("/clubs")
                .then().log().all()
                .extract()
                .as(Map.class)
                .get("data");

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
*/
