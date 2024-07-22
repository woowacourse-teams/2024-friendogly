package com.woowacourse.friendogly.docs;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.woowacourse.friendogly.auth.AuthArgumentResolver;
import com.woowacourse.friendogly.pet.controller.PetController;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.SizeType;
import com.woowacourse.friendogly.pet.dto.request.SavePetRequest;
import com.woowacourse.friendogly.pet.dto.response.SavePetResponse;
import com.woowacourse.friendogly.pet.service.PetCommandService;
import com.woowacourse.friendogly.pet.service.PetQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

import java.time.LocalDate;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
public class PetApiDocsTest extends RestDocsTest {

    @MockBean
    private PetCommandService petCommandService;

    @MockBean
    private PetQueryService petQueryService;

    @Autowired
    private AuthArgumentResolver authArgumentResolver;

    @DisplayName("반려견 등록 문서화")
    @Test
    void savePet_Success() throws Exception {
        Long loginMemberId = 1L;
        SavePetRequest request = new SavePetRequest(
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                SizeType.SMALL.toString(),
                Gender.FEMALE.toString(),
                "https://google.com"
        );
        SavePetResponse response = new SavePetResponse(
                1L,
                loginMemberId,
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                SizeType.SMALL.toString(),
                Gender.FEMALE.toString(),
                "https://google.com"
        );

        Mockito.when(petCommandService.savePet(any(), any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/pets")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, loginMemberId.toString()))
                .andExpect(status().isCreated())
                .andDo(MockMvcRestDocumentationWrapper.document("pet-save-201",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Pet API")
                                .summary("반려견 등록 API")
                                .requestHeaders(
                                        headerWithName("Authorization").description("로그인한 회원 id")
                                )
                                .requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("반려견 이름"),
                                        fieldWithPath("description").type(JsonFieldType.STRING).description("반려견 한 줄 소개"),
                                        fieldWithPath("birthDate").type(JsonFieldType.STRING).description("반려견 생년월일: yyyy-MM-dd"),
                                        fieldWithPath("sizeType").type(JsonFieldType.STRING).description("반려견 크기: SMALL, MEDIUM, LARGE"),
                                        fieldWithPath("gender").type(JsonFieldType.STRING).description("반려견 성별: MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED"),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("반려견 이미지 URL")
                                )
                                .responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("반려견 id"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("반려견의 주인 id"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("반려견 이름"),
                                        fieldWithPath("description").type(JsonFieldType.STRING).description("반려견 한 줄 소개"),
                                        fieldWithPath("birthDate").type(JsonFieldType.STRING).description("반려견 생년월일: yyyy-MM-dd"),
                                        fieldWithPath("sizeType").type(JsonFieldType.STRING).description("반려견 크기: SMALL, MEDIUM, LARGE"),
                                        fieldWithPath("gender").type(JsonFieldType.STRING).description("반려견 성별: MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED"),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("반려견 이미지 URL")
                                )
                                .requestSchema(Schema.schema("SavePetRequest"))
                                .responseSchema(Schema.schema("SavePetResponse"))
                                .build()))
                );
    }

    @Override
    protected Object controller() {
        return new PetController(petQueryService, petCommandService);
    }
}
