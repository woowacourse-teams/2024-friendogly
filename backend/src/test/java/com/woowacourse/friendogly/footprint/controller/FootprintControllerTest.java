package com.woowacourse.friendogly.footprint.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.friendogly.footprint.dto.request.FindNearFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.request.SaveFootprintRequest;
import com.woowacourse.friendogly.footprint.dto.response.FindNearFootprintResponse;
import com.woowacourse.friendogly.footprint.service.FootprintCommandService;
import com.woowacourse.friendogly.footprint.service.FootprintQueryService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(FootprintController.class)
class FootprintControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FootprintCommandService footprintCommandService;

    @MockBean
    private FootprintQueryService footprintQueryService;

    @DisplayName("발자국 저장")
    @Test
    void save() throws Exception {
        SaveFootprintRequest request = new SaveFootprintRequest(1L, 37.5173316, 127.1011661);

        given(footprintCommandService.save(request))
            .willReturn(1L);

        mockMvc
            .perform(post("/footprints")
                .content(objectMapper.writeValueAsString(request))
                .contentType(APPLICATION_JSON))
            .andDo(print())
            .andDo(document("footprints/save",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("memberId").description("사용자 ID"),
                    fieldWithPath("latitude").description("현재 위치의 위도"),
                    fieldWithPath("longitude").description("현재 위치의 경도")
                ),
                responseHeaders(
                    headerWithName(LOCATION).description("Location")
                )
            ))
            .andExpect(status().isCreated())
            .andExpect(header().string(LOCATION, "/footprints/1"));
    }

    @DisplayName("주변 발자국 조회")
    @Test
    void findNear() throws Exception {
        FindNearFootprintRequest request = new FindNearFootprintRequest(37.5173316, 127.1011661);

        given(footprintQueryService.findNear(request))
            .willReturn(
                List.of(
                    new FindNearFootprintResponse(1L, 37.5136533, 127.0983182, LocalDateTime.now().minusMinutes(10)),
                    new FindNearFootprintResponse(3L, 37.5131474, 127.1042528, LocalDateTime.now().minusMinutes(20)),
                    new FindNearFootprintResponse(6L, 37.5171728, 127.1047797, LocalDateTime.now().minusMinutes(30)),
                    new FindNearFootprintResponse(11L, 37.516183, 127.1068874, LocalDateTime.now().minusMinutes(40))
                )
            );

        mockMvc
            .perform(get("/footprints/near")
                .param("latitude", "37.5173316")
                .param("longitude", "127.1011661"))
            .andDo(print())
            .andDo(document("footprints/near",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                queryParameters(
                    parameterWithName("latitude").description("현재 위치의 위도"),
                    parameterWithName("longitude").description("현재 위치의 경도")
                ),
                responseFields(
                    fieldWithPath("[].memberId").description("발자국을 찍은 사용자 ID"),
                    fieldWithPath("[].latitude").description("발자국 위치의 위도"),
                    fieldWithPath("[].longitude").description("발자국 위치의 경도"),
                    fieldWithPath("[].createdAt").description("발자국 생성 시간")
                )
            ))
            .andExpect(status().isOk());
    }
}
