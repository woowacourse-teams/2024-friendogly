package com.woowacourse.friendogly.docs;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import com.woowacourse.friendogly.club.controller.ClubController;
import com.woowacourse.friendogly.club.domain.FilterCondition;
import com.woowacourse.friendogly.club.domain.Status;
import com.woowacourse.friendogly.club.dto.request.FindSearchingClubRequest;
import com.woowacourse.friendogly.club.dto.request.SaveClubMemberRequest;
import com.woowacourse.friendogly.club.dto.request.SaveClubRequest;
import com.woowacourse.friendogly.club.dto.response.ClubMemberDetailResponse;
import com.woowacourse.friendogly.club.dto.response.ClubPetDetailResponse;
import com.woowacourse.friendogly.club.dto.response.FindClubResponse;
import com.woowacourse.friendogly.club.dto.response.FindSearchingClubResponse;
import com.woowacourse.friendogly.club.dto.response.SaveClubMemberResponse;
import com.woowacourse.friendogly.club.dto.response.SaveClubResponse;
import com.woowacourse.friendogly.club.service.ClubCommandService;
import com.woowacourse.friendogly.club.service.ClubQueryService;
import com.woowacourse.friendogly.exception.FriendoglyException;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.SizeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;


public class ClubApiDocsTest extends RestDocsTest {

    @Mock
    private ClubCommandService clubCommandService;

    @Mock
    private ClubQueryService clubQueryService;


    @DisplayName("필터링 조건을 통해 모임 리스트를 조회한다.")
    @Test
    void findSearching_200() throws Exception {
        FindSearchingClubRequest request = new FindSearchingClubRequest(
                FilterCondition.ALL.name(),
                "서울특별시 송파구 신청동 잠실 6동",
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );

        List<FindSearchingClubResponse> responses = List.of(new FindSearchingClubResponse(
                        1L,
                        "모임 제목1",
                        "모임 본문 내용1",
                        "브라운",
                        "서울특별시 송파구 신정동 잠실 6동",
                        Status.OPEN,
                        LocalDateTime.now(),
                        Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                        Set.of(SizeType.SMALL),
                        4,
                        1,
                        "https:/clubImage1.com",
                        List.of("https://petImage1.com")
                ),
                new FindSearchingClubResponse(
                        2L,
                        "모임 제목2",
                        "모임 본문 내용2",
                        "레아",
                        "서울특별시 송파구 신정동 잠실 6동",
                        Status.OPEN,
                        LocalDateTime.now(),
                        Set.of(Gender.MALE, Gender.FEMALE_NEUTERED, Gender.MALE_NEUTERED),
                        Set.of(SizeType.MEDIUM),
                        3,
                        1,
                        "https:/clubImage2.com",
                        List.of("https://petImage1.com")
                )
        );

        when(clubQueryService.findSearching(anyLong(), any()))
                .thenReturn(responses);

        mockMvc.perform(get("/clubs/searching")
                        .header(HttpHeaders.AUTHORIZATION, 1L)
                        .param("filterCondition", request.filterCondition())
                        .param("address", request.address())
                        .param("genderParams", request.genderParams().stream().map(Enum::name).toArray(String[]::new))
                        .param("sizeParams", request.sizeParams().stream().map(Enum::name).toArray(String[]::new)))
                .andExpect(status().isOk())
                .andDo(document("clubs/findSearching/200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 검색 조회 API")
                                .queryParameters(
                                        parameterWithName("filterCondition").description("모임의 필터 조건 (ALL, OPEN, ABLE_TO_JOIN)"),
                                        parameterWithName("address").description("모임의 주소"),
                                        parameterWithName("genderParams").description("모임에 참여가능한 팻 성별"),
                                        parameterWithName("sizeParams").description("모임에 참여가능한 팻 크기"))
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("모임 식별자"),
                                        fieldWithPath("data.[].title").type(JsonFieldType.STRING).description("모임 제목"),
                                        fieldWithPath("data.[].content").type(JsonFieldType.STRING).description("모임 본문"),
                                        fieldWithPath("data.[].ownerMemberName").type(JsonFieldType.STRING).description("모임 방장 이름"),
                                        fieldWithPath("data.[].address").type(JsonFieldType.STRING).description("모임의 주소"),
                                        fieldWithPath("data.[].status").type(JsonFieldType.STRING).description("모임 상태(OPEN , CLOSED)"),
                                        fieldWithPath("data.[].createdAt").type(JsonFieldType.STRING).description("모임 생성 시간(LocalDateTime)"),
                                        fieldWithPath("data.[].allowedGender").type(JsonFieldType.ARRAY).description("허용되는 팻 성별(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED)"),
                                        fieldWithPath("data.[].allowedSize").type(JsonFieldType.ARRAY).description("허용되는 팻 크기(SMALL,MEDIUM,LARGE)"),
                                        fieldWithPath("data.[].memberCapacity").type(JsonFieldType.NUMBER).description("모임 최대 인원"),
                                        fieldWithPath("data.[].currentMemberCount").type(JsonFieldType.NUMBER).description("모임 현재 인원"),
                                        fieldWithPath("data.[].imageUrl").type(JsonFieldType.STRING).description("모임 프로필 사진"),
                                        fieldWithPath("data.[].petImageUrls").type(JsonFieldType.ARRAY).description("모임 리스트에 나오는 팻 사진 url 리스트"))
                                .responseSchema(Schema.schema("FindSearchingClubResponse"))
                                .build()))
                );
    }

    @DisplayName("ID를 통해 모임의 상세 정보를 조회한다.")
    @Test
    void findById_200() throws Exception {
        FindClubResponse response = new FindClubResponse(
                1L,
                "모임 제목1",
                "모임 본문 내용1",
                "브라운",
                "서울특별시 송파구 신정동 잠실 6동",
                Status.OPEN,
                LocalDateTime.now(),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL),
                4,
                1,
                "https://clubImage1.com",
                "https://OwnerProfileImage.com",
                true,
                true,
                false,
                List.of(new ClubMemberDetailResponse(2L, "정지호", "https://땡이 영공때 사진.com"),
                        new ClubMemberDetailResponse(3L, "쪙찌효", "https://땡이 근공때 사진.com")),
                List.of(new ClubPetDetailResponse(1L, "땡이", "https://땡이의 귀여운 이미지.com"),
                        new ClubPetDetailResponse(2L, "땡이 동생", "https://땡이 동생의 귀여운 이미지.com"))
        );

        when(clubQueryService.findById(anyLong(), anyLong()))
                .thenReturn(response);

        mockMvc.perform(get("/clubs/{id}",1L)
                .header(HttpHeaders.AUTHORIZATION, 2L))
                .andExpect(status().isOk())
                .andDo(document("clubs/findById/200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 상세 조회 API")
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("모임 식별자"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("모임 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("모임 본문"),
                                        fieldWithPath("data.ownerMemberName").type(JsonFieldType.STRING).description("모임 방장 이름"),
                                        fieldWithPath("data.address").type(JsonFieldType.STRING).description("모임의 주소"),
                                        fieldWithPath("data.status").type(JsonFieldType.STRING).description("모임 상태(OPEN , CLOSED)"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("모임 생성 시간(LocalDateTime)"),
                                        fieldWithPath("data.allowedGender").type(JsonFieldType.ARRAY).description("허용되는 팻 성별(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED)"),
                                        fieldWithPath("data.allowedSize").type(JsonFieldType.ARRAY).description("허용되는 팻 크기(SMALL,MEDIUM,LARGE)"),
                                        fieldWithPath("data.memberCapacity").type(JsonFieldType.NUMBER).description("모임 최대 인원"),
                                        fieldWithPath("data.currentMemberCount").type(JsonFieldType.NUMBER).description("모임 현재 인원"),
                                        fieldWithPath("data.imageUrl").type(JsonFieldType.STRING).description("모임 프로필 사진"),
                                        fieldWithPath("data.ownerImageUrl").type(JsonFieldType.STRING).description("모임 방장 프로필 사진"),
                                        fieldWithPath("data.isMine").type(JsonFieldType.BOOLEAN).description("현재 로그인한 사용자가 방장인지 여부"),
                                        fieldWithPath("data.alreadyParticipate").type(JsonFieldType.BOOLEAN).description("현재 로그인한 사용자가 모임에 참여 중인지 여부"),
                                        fieldWithPath("data.canParticipate").type(JsonFieldType.BOOLEAN).description("현재 로그인한 사용자가 모임에 참여 가능한지 여부"),
                                        fieldWithPath("data.memberDetails[].id").type(JsonFieldType.NUMBER).description("모임에 참여한 사용자 id"),
                                        fieldWithPath("data.memberDetails[].name").type(JsonFieldType.STRING).description("모임에 참여한 사용자 이름"),
                                        fieldWithPath("data.memberDetails[].imageUrl").type(JsonFieldType.STRING).description("모임에 참여한 사용자 이미지 URL"),
                                        fieldWithPath("data.petDetails[].id").type(JsonFieldType.NUMBER).description("모임에 참여한 반려견 id"),
                                        fieldWithPath("data.petDetails[].name").type(JsonFieldType.STRING).description("모임에 참여한 반려견 이름"),
                                        fieldWithPath("data.petDetails[].imageUrl").type(JsonFieldType.STRING).description("모임에 참여한 반려견 이미지 URL"))
                                .responseSchema(Schema.schema("FindClubResponse"))
                                .build()))
                );
    }

    @DisplayName("모임을 생성한다.")
    @Test
    void save_201() throws Exception {
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
        SaveClubResponse response = new SaveClubResponse(
                1L,
                "모임 제목",
                "모임 내용",
                "브라운",
                "서울특별시 송파구 신정동 잠실 5동",
                Status.OPEN,
                LocalDateTime.of(2024, 7, 23, 11, 5),
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL),
                5,
                1,
                "https://clubImage.com",
                List.of("https://pet1ImageUrl.com", "http://pet2ImageUrl"),
                true
        );

        when(clubCommandService.save(any(), any(SaveClubRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/clubs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("clubs/post/201",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 생성 API")
                                .requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).description("로그인 중 회원 정보"))
                                .requestFields(
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("모임 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("모임 내용"),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("모임 프로필 사진 url"),
                                        fieldWithPath("address").type(JsonFieldType.STRING).description("모임 주소"),
                                        fieldWithPath("allowedGenders").type(JsonFieldType.ARRAY).description("참여가능한 강아지 성별"),
                                        fieldWithPath("allowedSizes").type(JsonFieldType.ARRAY).description("참여가능한 강아지 사이즈"),
                                        fieldWithPath("memberCapacity").type(JsonFieldType.NUMBER).description("모임 최대 인원"),
                                        fieldWithPath("participatingPetsId").type(JsonFieldType.ARRAY).description("모임에 참여하는 방장 강아지의 ID 리스트")
                                )
                                .responseHeaders(
                                        headerWithName(HttpHeaders.LOCATION).description("생성된 모임 리소스 Location"))
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("모임 식별자"),
                                        fieldWithPath("data.title").type(JsonFieldType.STRING).description("모임 제목"),
                                        fieldWithPath("data.content").type(JsonFieldType.STRING).description("모임 본문"),
                                        fieldWithPath("data.ownerMemberName").type(JsonFieldType.STRING).description("모임 방장 이름"),
                                        fieldWithPath("data.address").type(JsonFieldType.STRING).description("모임의 주소"),
                                        fieldWithPath("data.status").type(JsonFieldType.STRING).description("모임 상태(OPEN , CLOSED)"),
                                        fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("모임 생성 시간(LocalDateTime)"),
                                        fieldWithPath("data.allowedSize").type(JsonFieldType.ARRAY).description("허용되는 팻 크기(SMALL,MEDIUM,LARGE)"),
                                        fieldWithPath("data.allowedGender").type(JsonFieldType.ARRAY).description("허용되는 팻 성별(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED)"),
                                        fieldWithPath("data.memberCapacity").type(JsonFieldType.NUMBER).description("모임 최대 인원"),
                                        fieldWithPath("data.currentMemberCount").type(JsonFieldType.NUMBER).description("모임 현재 인원(대부분의 경우 1)"),
                                        fieldWithPath("data.imageUrl").type(JsonFieldType.STRING).description("모임 프로필 사진"),
                                        fieldWithPath("data.petImageUrls").type(JsonFieldType.ARRAY).description("모임 리스트에 참여하는 팻 프로필 url"),
                                        fieldWithPath("data.isMine").type(JsonFieldType.BOOLEAN).description("현재 회원의 글인지 판단하는 값(대부분의 경우 true)"))
                                .requestSchema(Schema.schema("saveClubRequest"))
                                .responseSchema(Schema.schema("saveClubResponse"))
                                .build()))
                );
    }

    @DisplayName("모임에 참여한다.")
    @Test
    void saveClubMember_201() throws Exception {
        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(1L));
        SaveClubMemberResponse response = new SaveClubMemberResponse(1L);
        when(clubCommandService.joinClub(any(), any(), any()))
                .thenReturn(response);

        mockMvc.perform(post("/clubs/{clubId}/members", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("clubs/members/201",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 참여 API")
                                .pathParameters(
                                        parameterWithName("clubId").type(SimpleType.NUMBER).description("참여하는 모임의 ID"))
                                .requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).type(SimpleType.NUMBER).description("로그인 중인 회원 ID"))
                                .requestFields(
                                        fieldWithPath("participatingPetsId").type(JsonFieldType.ARRAY).description("참여하는 팻 ID 리스트")
                                )
                                .responseHeaders(
                                        headerWithName(HttpHeaders.LOCATION).type(SimpleType.STRING).description("모임-회원 연관관계 리소스 Location")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("모임-회원 연관관계 ID(추후 채팅 생기면 변경)"))
                                .responseSchema(Schema.schema("SaveClubMemberResponse"))
                                .build())
                ));
    }

    @DisplayName("모임 참여에 실패한 경우(이미 참여 중 또는 참여할 수 없는 모임 등)")
    @Test
    void saveClubMember_400() throws Exception {
        SaveClubMemberRequest request = new SaveClubMemberRequest(List.of(1L));

        when(clubCommandService.joinClub(any(), any(), any()))
                .thenThrow(new FriendoglyException("예외 메세지"));

        mockMvc.perform(post("/clubs/{clubId}/members", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(document("clubs/members/400",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 참여 API")
                                .pathParameters(
                                        parameterWithName("clubId").type(SimpleType.NUMBER).description("참여하는 모임의 ID"))
                                .requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).type(SimpleType.NUMBER).description("로그인 중인 회원 ID"))
                                .requestFields(
                                        fieldWithPath("participatingPetsId").type(JsonFieldType.ARRAY).description("참여하는 팻 ID 리스트")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.errorCode").type(JsonFieldType.STRING).description("에러 코드"),
                                        fieldWithPath("data.errorMessage").type(JsonFieldType.STRING).description("에러메세지"),
                                        fieldWithPath("data.detail").type(JsonFieldType.ARRAY).description("에러 디테일")
                                )
                                .build())
                ));
    }

    @DisplayName("모임에서 빠지면 204를 반환한다.")
    @Test
    void deleteClubMember_204() throws Exception {

        doNothing()
                .when(clubCommandService)
                .deleteClubMember(any(), any());

        mockMvc.perform(delete("/clubs/{clubId}/members", 1L)
                        .header(HttpHeaders.AUTHORIZATION, 1L))
                .andExpect(status().isNoContent())
                .andDo(document("clubs/{clubId}/members/400",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 탈퇴 API")
                                .requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).type(SimpleType.NUMBER)
                                        .description("로그인 중인 회원 ID"))
                                .pathParameters(
                                        parameterWithName("clubId").type(SimpleType.NUMBER).description("탈퇴하는 모임의 ID")
                                )
                                .build())
                ));
    }

    @DisplayName("모임에서 탈퇴 중 예외가 발생한다. 400")
    @Test
    void deleteClubMember_400() throws Exception {

        doThrow(new FriendoglyException("예외 메세지"))
                .when(clubCommandService)
                .deleteClubMember(any(), any());

        mockMvc.perform(delete("/clubs/{clubId}/members", 1L)
                        .header(HttpHeaders.AUTHORIZATION, 1L))
                .andExpect(status().isBadRequest())
                .andDo(document("clubs/{clubId}/members/400",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 탈퇴 API")
                                .requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).type(SimpleType.NUMBER)
                                        .description("로그인 중인 회원 ID"))
                                .pathParameters(
                                        parameterWithName("clubId").type(SimpleType.NUMBER).description("탈퇴하는 모임의 ID")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.errorCode").type(JsonFieldType.STRING).description("에러 코드"),
                                        fieldWithPath("data.errorMessage").type(JsonFieldType.STRING).description("에러메세지"),
                                        fieldWithPath("data.detail").type(JsonFieldType.ARRAY).description("에러 디테일")
                                )
                                .build())
                ));
    }

    @Override
    protected Object controller() {
        return new ClubController(clubCommandService, clubQueryService);
    }
}
