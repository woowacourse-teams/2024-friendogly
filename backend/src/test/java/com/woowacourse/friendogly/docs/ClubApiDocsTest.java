package com.woowacourse.friendogly.docs;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.epages.restdocs.apispec.SimpleType;
import com.woowacourse.friendogly.auth.AuthArgumentResolver;
import com.woowacourse.friendogly.club.controller.ClubController;
import com.woowacourse.friendogly.club.domain.Status;
import com.woowacourse.friendogly.club.dto.request.FindSearchingClubRequest;
import com.woowacourse.friendogly.club.dto.request.SaveClubMemberRequest;
import com.woowacourse.friendogly.club.dto.request.SaveClubRequest;
import com.woowacourse.friendogly.club.dto.response.FindSearchingClubResponse;
import com.woowacourse.friendogly.club.dto.response.SaveClubMemberResponse;
import com.woowacourse.friendogly.club.dto.response.SaveClubResponse;
import com.woowacourse.friendogly.club.service.ClubCommandService;
import com.woowacourse.friendogly.club.service.ClubQueryService;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.SizeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(ClubController.class)
public class ClubApiDocsTest extends RestDocsTest {

    @MockBean
    private ClubCommandService clubCommandService;

    @MockBean
    private ClubQueryService clubQueryService;

    @Autowired
    private AuthArgumentResolver authArgumentResolver;

    @DisplayName("필터링 조건을 통해 모임 리스트를 조회한다.")
    @Test
    void findSearching_200() throws Exception {
        FindSearchingClubRequest request = new FindSearchingClubRequest(
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

        when(clubQueryService.findSearching(request))
                .thenReturn(responses);

        mockMvc.perform(get("/clubs/searching")
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
                                        parameterWithName("address").description("모임의 주소"),
                                        parameterWithName("genderParams").description("모임에 참여가능한 팻 성별"),
                                        parameterWithName("sizeParams").description("모임에 참여가능한 팻 크기"))
                                .responseFields(
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("모임 식별자"),
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("모임 제목"),
                                        fieldWithPath("[].content").type(JsonFieldType.STRING).description("모임 본문"),
                                        fieldWithPath("[].ownerMemberName").type(JsonFieldType.STRING)
                                                .description("모임 방장 이름"),
                                        fieldWithPath("[].address").type(JsonFieldType.STRING).description("모임의 주소"),
                                        fieldWithPath("[].status").type(JsonFieldType.STRING)
                                                .description("모임 상태(OPEN , CLOSED)"),
                                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING)
                                                .description("모임 생성 시간(LocalDateTime)"),
                                        fieldWithPath("[].allowedGender").type(JsonFieldType.ARRAY)
                                                .description("허용되는 팻 성별(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED)"),
                                        fieldWithPath("[].allowedSize").type(JsonFieldType.ARRAY)
                                                .description("허용되는 팻 크기(SMALL,MEDIUM,LARGE)"),
                                        fieldWithPath("[].memberCapacity").type(JsonFieldType.NUMBER)
                                                .description("모임 최대 인원"),
                                        fieldWithPath("[].currentMemberCount").type(JsonFieldType.NUMBER)
                                                .description("모임 현재 인원"),
                                        fieldWithPath("[].imageUrl").type(JsonFieldType.STRING)
                                                .description("모임 프로필 사진"),
                                        fieldWithPath("[].petImageUrls").type(JsonFieldType.ARRAY)
                                                .description("모임 리스트에 팻 사진 url"))
                                .responseSchema(Schema.schema("FindSearchingClubResponse"))
                                .build()))
                );
    }
    //TODO: ErrorResponse 구현 후 실패 케이스 문서화
   /* @DisplayName("필터링 조건을 통해 모임 리스트를 조회 400")
    @Test
    void findSearching_400() throws Exception {
        when(clubQueryService.findSearching(any()))
                .thenThrow(FriendoglyException.class);

        mockMvc.perform(get("/clubs/searching")
                        .param("address", "서울특별시 송파구 신천동 잠실 6동")
                        .param("genderParams", "남자,중성화 남자")
                        .param("sizeParams", "SMALL"))
                .andExpect(status().isBadRequest())
                .andDo(document("clubs/findSearching/400",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 검색 조회 API")
                                .responseFields(
                                        fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태 코드"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지")
                                )
                                .build()
                        )));
    }*/

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
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING)
                                                .description("모임 프로필 사진 url"),
                                        fieldWithPath("address").type(JsonFieldType.STRING).description("모임 주소"),
                                        fieldWithPath("allowedGenders").type(JsonFieldType.ARRAY)
                                                .description("참여가능한 강아지 성별"),
                                        fieldWithPath("allowedSizes").type(JsonFieldType.ARRAY)
                                                .description("참여가능한 강아지 사이즈"),
                                        fieldWithPath("memberCapacity").type(JsonFieldType.NUMBER)
                                                .description("모임 최대 인원"),
                                        fieldWithPath("participatingPetsId").type(JsonFieldType.ARRAY)
                                                .description("모임에 참여하는 방장 강아지의 ID 리스트")
                                )
                                .responseHeaders(
                                        headerWithName(HttpHeaders.LOCATION).description("생성된 모임 리소스 Location"))
                                .responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("모임 식별자"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("모임 제목"),
                                        fieldWithPath("content").type(JsonFieldType.STRING).description("모임 본문"),
                                        fieldWithPath("ownerMemberName").type(JsonFieldType.STRING)
                                                .description("모임 방장 이름"),
                                        fieldWithPath("address").type(JsonFieldType.STRING).description("모임의 주소"),
                                        fieldWithPath("status").type(JsonFieldType.STRING)
                                                .description("모임 상태(OPEN , CLOSED)"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING)
                                                .description("모임 생성 시간(LocalDateTime)"),
                                        fieldWithPath("allowedSize").type(JsonFieldType.ARRAY)
                                                .description("허용되는 팻 크기(SMALL,MEDIUM,LARGE)"),
                                        fieldWithPath("allowedGender").type(JsonFieldType.ARRAY)
                                                .description("허용되는 팻 성별(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED)"),
                                        fieldWithPath("memberCapacity").type(JsonFieldType.NUMBER)
                                                .description("모임 최대 인원"),
                                        fieldWithPath("currentMemberCount").type(JsonFieldType.NUMBER)
                                                .description("모임 현재 인원(웬만해선 1)"),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING)
                                                .description("모임 프로필 사진"),
                                        fieldWithPath("petImageUrls").type(JsonFieldType.ARRAY)
                                                .description("모임 리스트에 참여하는 팻 프로필 url"),
                                        fieldWithPath("isMine").type(JsonFieldType.BOOLEAN)
                                                .description("현재 회원의 글인지 판단하는 값(웬만해선 true)"))
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
        when(clubCommandService.saveClubMember(any(), any(), any()))
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
                                .requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).type(SimpleType.NUMBER)
                                        .description("로그인 중인 회원 ID"))
                                .requestFields(
                                        fieldWithPath("participatingPetsId").type(JsonFieldType.ARRAY)
                                                .description("참여하는 팻 ID 리스트")
                                )
                                .responseHeaders(
                                        headerWithName(HttpHeaders.LOCATION).type(SimpleType.STRING)
                                                .description("모임-회원 연관관계 리소스 Location")
                                )
                                .responseFields(fieldWithPath("clubMemberId").type(JsonFieldType.NUMBER)
                                        .description("모임-회원 연관관계 ID(추후 채팅 생기면 변경)"))
                                .responseSchema(Schema.schema("SaveClubMemberResponse"))
                                .build())
                ));
    }

/*    @DisplayName("이미 모임에 참여 중이거나, 참여할 수 없는 경우")
    @Test
    void saveClubMember_400() throws Exception {

        when(clubCommandService.saveClubMember(any(), any()))
                .thenThrow(FriendoglyException.class);

        mockMvc.perform(post("/clubs/{clubId}/members", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("clubs/members/400",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Club API")
                                .summary("모임 참여 API")
                                .pathParameters(
                                        parameterWithName("clubId").type(SimpleType.NUMBER).description("참여하는 모임의 ID"))
                                .requestHeaders(headerWithName(HttpHeaders.AUTHORIZATION).type(SimpleType.NUMBER)
                                        .description("로그인 중인 회원 ID"))
                                //에러 Response
                                .build())
                ));
    }*/

    @Override
    protected Object controller() {
        return new ClubController(clubCommandService, clubQueryService);
    }
}
