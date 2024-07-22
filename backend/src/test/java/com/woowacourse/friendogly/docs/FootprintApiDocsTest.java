package com.woowacourse.friendogly.docs;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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
import com.woowacourse.friendogly.footprint.dto.request.FindNearFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.response.FindMyLatestFootprintTimeResponse;
import com.woowacourse.friendogly.footprint.dto.response.FindNearFootprintResponse;
import com.woowacourse.friendogly.footprint.dto.response.FindOneFootprintResponse;
import com.woowacourse.friendogly.footprint.dto.response.SaveFootprintResponse;
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
        SaveFootprintResponse response = new SaveFootprintResponse(1L, 37.5173316, 127.1011661);

        given(footprintCommandService.save(any(Long.class), eq(request)))
                .willReturn(response);

        mockMvc
                .perform(post("/footprints")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andDo(document("footprints/save",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("발자국 저장 API")
                                .summary("발자국 저장 API")
                                .requestFields(
                                        fieldWithPath("latitude").description("현재 위치의 위도"),
                                        fieldWithPath("longitude").description("현재 위치의 경도")
                                )
                                .responseHeaders(
                                        headerWithName(LOCATION).description("Location")
                                )
                                .responseFields(
                                        fieldWithPath("id").description("생성된 발자국 ID"),
                                        fieldWithPath("latitude").description("생성된 발자국의 위도"),
                                        fieldWithPath("longitude").description("생성된 발자국의 경도")
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
                "최강지호",
                "땡이",
                "우리 귀여운 땡이입니다",
                LocalDate.now().minusYears(1),
                SizeType.MEDIUM,
                Gender.FEMALE_NEUTERED,
                "https://picsum.photos/200",
                LocalDateTime.now(),
                true
        );

        given(footprintQueryService.findOne(any(Long.class), any(Long.class)))
                .willReturn(response);

        mockMvc
                .perform(get("/footprints/{footprintId}", 1L))
                .andDo(print())
                .andDo(document("footprints/findOne",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("발자국 세부정보 단건 조회 API")
                                .summary("발자국 세부정보 단건 조회 API")
                                .pathParameters(
                                        parameterWithName("footprintId").description("발자국 ID")
                                )
                                .responseFields(
                                        fieldWithPath("memberName").description("발자국을 찍은 회원의 Member ID"),
                                        fieldWithPath("petName").description("발자국을 회원의 강아지 이름"),
                                        fieldWithPath("petDescription").description("발자국을 회원의 강아지 설명"),
                                        fieldWithPath("petBirthDate").description("발자국을 찍은 회원의 강아지 생일"),
                                        fieldWithPath("petSizeType").description("발자국을 찍은 회원의 강아지 사이즈"),
                                        fieldWithPath("petGender").description("발자국을 찍은 회원의 강아지 성별(중성화 포함)"),
                                        fieldWithPath("footprintImageUrl").description("발자국의 이미지 URL"),
                                        fieldWithPath("createdAt").description("발자국이 생성된 시간"),
                                        fieldWithPath("isMine").description("내 발자국인지 여부 (내 발자국이면 true)")
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
        FindNearFootprintRequest request = new FindNearFootprintRequest(37.5173316, 127.1011661);
        List<FindNearFootprintResponse> response = List.of(
                new FindNearFootprintResponse(
                        1L, 37.5136533, 127.0983182, LocalDateTime.now().minusMinutes(10), true,
                        "https://picsum.photos/200"),
                new FindNearFootprintResponse(
                        3L, 37.5131474, 127.1042528, LocalDateTime.now().minusMinutes(20), false, ""),
                new FindNearFootprintResponse(
                        6L, 37.5171728, 127.1047797, LocalDateTime.now().minusMinutes(30), false,
                        "https://picsum.photos/300"),
                new FindNearFootprintResponse(
                        11L, 37.516183, 127.1068874, LocalDateTime.now().minusMinutes(40), true,
                        "https://picsum.photos/250")
        );

        given(footprintQueryService.findNear(any(Long.class), eq(request)))
                .willReturn(response);

        mockMvc
                .perform(get("/footprints/near")
                        .param("latitude", "37.5173316")
                        .param("longitude", "127.1011661"))
                .andDo(print())
                .andDo(document("footprints/near",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("주변 발자국 조회 API")
                                .summary("주변 발자국 조회 API")
                                .queryParameters(
                                        parameterWithName("latitude").description("현재 위치의 위도"),
                                        parameterWithName("longitude").description("현재 위치의 경도")
                                )
                                .responseFields(
                                        fieldWithPath("[].footprintId").description("발자국 ID"),
                                        fieldWithPath("[].latitude").description("발자국 위치의 위도"),
                                        fieldWithPath("[].longitude").description("발자국 위치의 경도"),
                                        fieldWithPath("[].createdAt").description("발자국 생성 시간"),
                                        fieldWithPath("[].isMine").description("나의 발자국인지 여부"),
                                        fieldWithPath("[].imageUrl").description("발자국에 할당된 이미지 URL")
                                )
                                .responseSchema(Schema.schema("FindNearFootprintResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("자신의 마지막 발자국 시간 조회")
    @Test
    void findMyLatestFootprintTime() throws Exception {
        FindMyLatestFootprintTimeResponse response = new FindMyLatestFootprintTimeResponse(
                LocalDateTime.now().minusHours(1)
        );

        given(footprintQueryService.findMyLatestFootprintTime(any(Long.class)))
                .willReturn(response);

        mockMvc
                .perform(get("/footprints/mine/latest"))
                .andDo(print())
                .andDo(document("footprints/findMyLatestFootprintTime",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("자신의 마지막 발자국 시간 조회 API")
                                .summary("자신의 마지막 발자국 시간 조회 API")
                                .responseFields(
                                        fieldWithPath("createdAt").description("자신의 가장 최근 발자국 생성 시간")
                                )
                                .responseSchema(Schema.schema("FindMyLatestFootprintTimeResponse"))
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
