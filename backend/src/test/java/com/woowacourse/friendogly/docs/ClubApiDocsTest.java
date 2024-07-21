package com.woowacourse.friendogly.docs;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.woowacourse.friendogly.club.controller.ClubController;
import com.woowacourse.friendogly.club.domain.Status;
import com.woowacourse.friendogly.club.dto.request.FindSearchingClubRequest;
import com.woowacourse.friendogly.club.dto.response.FindSearchingClubResponse;
import com.woowacourse.friendogly.club.service.ClubCommandService;
import com.woowacourse.friendogly.club.service.ClubQueryService;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.SizeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(ClubController.class)
public class ClubApiDocsTest extends RestDocsTest {

    @MockBean
    private ClubCommandService clubCommandService;

    @MockBean
    private ClubQueryService clubQueryService;

    @DisplayName("필터링 조건을 통해 모임 리스트를 조회한다.")
    @Test
    void findSearching_200() throws Exception {
        FindSearchingClubRequest request = new FindSearchingClubRequest(
                "서울특별시 송파구 신청동 잠실 6동",
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );

        List<FindSearchingClubResponse> responses = List.of(new FindSearchingClubResponse(
                        "모임 제목1",
                        "모임 본문 내용1",
                        "브라운",
                        "서울특별시 송파구 신정동 잠실 6동",
                        Status.OPEN,
                        LocalDateTime.now(),
                        Set.of(SizeType.SMALL),
                        Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                        4,
                        1,
                        List.of("https://petImage1.com")
                ),
                new FindSearchingClubResponse(
                        "모임 제목2",
                        "모임 본문 내용2",
                        "레아",
                        "서울특별시 송파구 신정동 잠실 6동",
                        Status.OPEN,
                        LocalDateTime.now(),
                        Set.of(SizeType.MEDIUM),
                        Set.of(Gender.MALE, Gender.FEMALE_NEUTERED, Gender.MALE_NEUTERED),
                        3,
                        1,
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
                                        fieldWithPath("[].title").type(JsonFieldType.STRING).description("모임 제목"),
                                        fieldWithPath("[].content").type(JsonFieldType.STRING).description("모임 본문"),
                                        fieldWithPath("[].ownerMemberName").type(JsonFieldType.STRING)
                                                .description("모임 방장 이름"),
                                        fieldWithPath("[].address").type(JsonFieldType.STRING).description("모임의 주소"),
                                        fieldWithPath("[].status").type(JsonFieldType.STRING)
                                                .description("모임 상태(OPEN , CLOSED)"),
                                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING)
                                                .description("모임 생성 시간(LocalDateTime)"),
                                        fieldWithPath("[].allowedSize").type(JsonFieldType.ARRAY)
                                                .description("허용되는 팻 크기(SMALL,MEDIUM,LARGE)"),
                                        fieldWithPath("[].allowedGender").type(JsonFieldType.ARRAY)
                                                .description("허용되는 팻 성별(MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED)"),
                                        fieldWithPath("[].memberCapacity").type(JsonFieldType.NUMBER)
                                                .description("모임 최대 인원"),
                                        fieldWithPath("[].currentMemberCount").type(JsonFieldType.NUMBER)
                                                .description("모임 현재 인원"),
                                        fieldWithPath("[].petImageUrls").type(JsonFieldType.ARRAY)
                                                .description("모임 리스트에 팻 사진 url"))
                                .responseSchema(Schema.schema("FindSearchingClubResponse"))
                                .build()))
                );
    }
    //TODO: ErrorResponse 구현 후 문서화
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


    @Override
    protected Object controller() {
        return new ClubController(clubCommandService, clubQueryService);
    }
}
