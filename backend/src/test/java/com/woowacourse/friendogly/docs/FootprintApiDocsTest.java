package com.woowacourse.friendogly.docs;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.woowacourse.friendogly.footprint.domain.WalkStatus.BEFORE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.woowacourse.friendogly.footprint.controller.FootprintController;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.response.FindMyLatestFootprintTimeAndPetExistenceResponse;
import com.woowacourse.friendogly.footprint.dto.response.FindNearFootprintResponse;
import com.woowacourse.friendogly.footprint.dto.response.FindOneFootprintResponse;
import com.woowacourse.friendogly.footprint.dto.response.SaveFootprintResponse;
import com.woowacourse.friendogly.footprint.dto.response.detail.PetDetail;
import com.woowacourse.friendogly.footprint.service.FootprintCommandService;
import com.woowacourse.friendogly.footprint.service.FootprintQueryService;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.SizeType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(FootprintController.class)
public class FootprintApiDocsTest extends RestDocsTest {

    @MockBean
    private FootprintQueryService footprintQueryService;

    @MockBean
    private FootprintCommandService footprintCommandService;

    @DisplayName("발자국 저장")
    @Test
    void save() throws Exception {
        SaveFootprintRequest request = new SaveFootprintRequest(37.5173316, 127.1011661);
        SaveFootprintResponse response = new SaveFootprintResponse(1L, 37.5173316, 127.1011661, LocalDateTime.now());

        given(footprintCommandService.save(any(), eq(request)))
                .willReturn(response);

        mockMvc
                .perform(post("/footprints")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "1"))
                .andDo(print())
                .andDo(document("footprints/save",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Footprint API")
                                .summary("발자국 저장 API")
                                .requestHeaders(
                                        headerWithName(AUTHORIZATION).description("로그인한 회원 ID")
                                )
                                .requestFields(
                                        fieldWithPath("latitude").description("현재 위치의 위도"),
                                        fieldWithPath("longitude").description("현재 위치의 경도")
                                )
                                .responseHeaders(
                                        headerWithName(LOCATION).description("Location")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.id").description("생성된 발자국 ID"),
                                        fieldWithPath("data.latitude").description("생성된 발자국의 위도"),
                                        fieldWithPath("data.longitude").description("생성된 발자국의 경도"),
                                        fieldWithPath("data.createdAt").description("발자국을 생성한 시간")
                                )
                                .requestSchema(Schema.schema("SaveFootprintRequest"))
                                .responseSchema(Schema.schema("SaveFootprintResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isCreated())
                .andExpect(header().string(LOCATION, "/footprints/1"));
    }

    @DisplayName("발자국 세부정보 단건 조회")
    @Test
    void findOne() throws Exception {
        FindOneFootprintResponse response = new FindOneFootprintResponse(
                1L,
                "최강지호",
                BEFORE,
                LocalDateTime.now(),
                List.of(
                        new PetDetail(
                                "땡이",
                                "땡이 귀여워요",
                                LocalDate.of(2020, 12, 10),
                                SizeType.MEDIUM,
                                Gender.FEMALE_NEUTERED,
                                "http://image.com"),
                        new PetDetail(
                                "두부",
                                "두부처럼 하얗습니다.",
                                LocalDate.of(2021, 10, 10),
                                SizeType.SMALL,
                                Gender.MALE,
                                "http://image.com")
                ),
                true
        );

        given(footprintQueryService.findOne(any(), any()))
                .willReturn(response);

        mockMvc
                .perform(get("/footprints/{footprintId}", 1L)
                        .header(AUTHORIZATION, 1L))
                .andDo(print())
                .andDo(document("footprints/findOne",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Footprint API")
                                .summary("발자국 세부정보 단건 조회 API")
                                .requestHeaders(
                                        headerWithName(AUTHORIZATION).description("로그인한 회원 ID")
                                )
                                .pathParameters(
                                        parameterWithName("footprintId").description("발자국 ID")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.memberId").description("발자국을 찍은 회원의 Member ID"),
                                        fieldWithPath("data.memberName").description("발자국을 찍은 회원의 Member ID"),
                                        fieldWithPath("data.walkStatus").description("발자국의 산책 상태"),
                                        fieldWithPath("data.changedWalkStatusTime").description("발자국의 산책 상태가 변경된 시간"),
                                        fieldWithPath("data.pets[].name").description("발자국을 회원의 강아지 이름"),
                                        fieldWithPath("data.pets[].description").description("발자국을 회원의 강아지 설명"),
                                        fieldWithPath("data.pets[].birthDate").description("발자국을 찍은 회원의 강아지 생일"),
                                        fieldWithPath("data.pets[].sizeType").description("발자국을 찍은 회원의 강아지 사이즈"),
                                        fieldWithPath("data.pets[].gender").description(
                                                "발자국을 찍은 회원의 강아지 성별(중성화 포함)"),
                                        fieldWithPath("data.pets[].imageUrl").description("발자국 찍은 회원의 강아지 사진"),
                                        fieldWithPath("data.isMine").description("내 발자국인지 여부 (내 발자국이면 true)")
                                )
                                .requestSchema(Schema.schema("FindOneFootprintRequest"))
                                .responseSchema(Schema.schema("FindOneFootprintResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("주변 발자국 조회")
    @Test
    void findNear() throws Exception {
        List<FindNearFootprintResponse> response = List.of(
                new FindNearFootprintResponse(
                        1L, 37.5136533, 127.0983182, BEFORE, LocalDateTime.now().minusMinutes(10), true),
                new FindNearFootprintResponse(
                        3L, 37.5131474, 127.1042528, BEFORE, LocalDateTime.now().minusMinutes(20), false),
                new FindNearFootprintResponse(
                        6L, 37.5171728, 127.1047797, BEFORE, LocalDateTime.now().minusMinutes(30), false),
                new FindNearFootprintResponse(
                        11L, 37.516183, 127.1068874, BEFORE, LocalDateTime.now().minusMinutes(40), true)
        );

        given(footprintQueryService.findNear(any(), any()))
                .willReturn(response);

        mockMvc
                .perform(get("/footprints/near")
                        .param("latitude", "37.5173316")
                        .param("longitude", "127.1011661")
                        .header(AUTHORIZATION, 1L))
                .andDo(print())
                .andDo(document("footprints/near",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Footprint API")
                                .summary("주변 발자국 조회 API")
                                .requestHeaders(
                                        headerWithName(AUTHORIZATION).description("로그인한 회원 ID")
                                )
                                .queryParameters(
                                        parameterWithName("latitude").description("현재 위치의 위도"),
                                        parameterWithName("longitude").description("현재 위치의 경도")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.[].footprintId").description("발자국 ID"),
                                        fieldWithPath("data.[].latitude").description("발자국 위치의 위도"),
                                        fieldWithPath("data.[].longitude").description("발자국 위치의 경도"),
                                        fieldWithPath("data.[].walkStatus").description("발자국의 산책 상태"),
                                        fieldWithPath("data.[].createdAt").description("발자국 생성 시간"),
                                        fieldWithPath("data.[].isMine").description("나의 발자국인지 여부")
                                )
                                .responseSchema(Schema.schema("FindNearFootprintResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("자신의 마지막 발자국 시간 및 펫 존재 여부 조회")
    @Test
    void findMyLatestFootprintTimeAndPetExistence() throws Exception {
        FindMyLatestFootprintTimeAndPetExistenceResponse response = new FindMyLatestFootprintTimeAndPetExistenceResponse(
                LocalDateTime.now().minusHours(1), true
        );

        given(footprintQueryService.findMyLatestFootprintTimeAndPetExistence(any()))
                .willReturn(response);

        mockMvc
                .perform(get("/footprints/mine/latest")
                        .header(AUTHORIZATION, 1L))
                .andDo(print())
                .andDo(document("footprints/findMyLatestFootprintTime",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Footprint API")
                                .summary("자신의 마지막 발자국 시간 조회 API")
                                .requestHeaders(
                                        headerWithName(AUTHORIZATION).description("로그인한 회원 ID")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.createdAt").description("자신의 가장 최근 발자국 생성 시간"),
                                        fieldWithPath("data.hasPet").description("자신의 펫 존재 여부 (1마리라도 펫 존재 = true)")
                                )
                                .responseSchema(Schema.schema("FindMyLatestFootprintTimeAndPetExistenceResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @Override
    protected Object controller() {
        return new FootprintController(footprintCommandService, footprintQueryService);
    }
}
