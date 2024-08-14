package com.happy.friendogly.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestPartFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.happy.friendogly.pet.controller.PetController;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.SizeType;
import com.happy.friendogly.pet.dto.request.SavePetRequest;
import com.happy.friendogly.pet.dto.request.UpdatePetRequest;
import com.happy.friendogly.pet.dto.response.FindPetResponse;
import com.happy.friendogly.pet.dto.response.SavePetResponse;
import com.happy.friendogly.pet.service.PetCommandService;
import com.happy.friendogly.pet.service.PetQueryService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

public class PetApiDocsTest extends RestDocsTest {

    @Mock
    private PetCommandService petCommandService;

    @Mock
    private PetQueryService petQueryService;

    @DisplayName("반려견 등록 문서화")
    @Test
    void savePet_Success() throws Exception {
        Long loginMemberId = 1L;
        SavePetRequest requestDto = new SavePetRequest(
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
        MockMultipartFile image = new MockMultipartFile("image", "image", MediaType.MULTIPART_FORM_DATA.toString(),
                "asdf".getBytes());
        MockMultipartFile request = new MockMultipartFile("request", "request", "application/json",
                objectMapper.writeValueAsBytes(requestDto));

        Mockito.when(petCommandService.savePet(any(), any(), any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.multipart("/pets")
                        .file(image)
                        .file(request)
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken()))
                .andExpect(status().isCreated())
                .andDo(MockMvcRestDocumentationWrapper.document("pet-save-201",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("image").description("강아지 프로필 이미지 파일"),
                                partWithName("request").description("강아지 등록 정보")
                        ),
                        requestPartFields(
                                "request",
                                fieldWithPath("name").type(JsonFieldType.STRING).description("반려견 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("반려견 한 줄 소개"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING)
                                        .description("반려견 생년월일: yyyy-MM-dd"),
                                fieldWithPath("sizeType").type(JsonFieldType.STRING)
                                        .description("반려견 크기: SMALL, MEDIUM, LARGE"),
                                fieldWithPath("gender").type(JsonFieldType.STRING)
                                        .description("반려견 성별: MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED"),
                                fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("반려견 이미지 URL")
                        ),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Pet API")
                                .summary("반려견 등록 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 accessToken")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("반려견 id"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER)
                                                .description("반려견의 주인 id"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("반려견 이름"),
                                        fieldWithPath("data.description").type(JsonFieldType.STRING)
                                                .description("반려견 한 줄 소개"),
                                        fieldWithPath("data.birthDate").type(JsonFieldType.STRING)
                                                .description("반려견 생년월일: yyyy-MM-dd"),
                                        fieldWithPath("data.sizeType").type(JsonFieldType.STRING)
                                                .description("반려견 크기: SMALL, MEDIUM, LARGE"),
                                        fieldWithPath("data.gender").type(JsonFieldType.STRING)
                                                .description("반려견 성별: MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED"),
                                        fieldWithPath("data.imageUrl").type(JsonFieldType.STRING)
                                                .description("반려견 이미지 URL")
                                )
                                .requestSchema(Schema.schema("SavePetRequest"))
                                .responseSchema(Schema.schema("SavePetResponse"))
                                .build()))
                );
    }

    @DisplayName("반려견 단건 조회 문서화")
    @Test
    void findById_Success() throws Exception {
        Long petId = 1L;
        FindPetResponse response = new FindPetResponse(
                petId,
                1L,
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                SizeType.SMALL.toString(),
                Gender.FEMALE.toString(),
                "https://google.com"
        );

        Mockito.when(petQueryService.findById(any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/pets/{id}", petId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("pet-findById-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Pet API")
                                .summary("반려견 단건 조회 API")
                                .pathParameters(
                                        parameterWithName("id").description("조회하려는 반려견 id")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("반려견 id"),
                                        fieldWithPath("data.memberId").type(JsonFieldType.NUMBER)
                                                .description("반려견의 주인 id"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("반려견 이름"),
                                        fieldWithPath("data.description").type(JsonFieldType.STRING)
                                                .description("반려견 한 줄 소개"),
                                        fieldWithPath("data.birthDate").type(JsonFieldType.STRING)
                                                .description("반려견 생년월일: yyyy-MM-dd"),
                                        fieldWithPath("data.sizeType").type(JsonFieldType.STRING)
                                                .description("반려견 크기: SMALL, MEDIUM, LARGE"),
                                        fieldWithPath("data.gender").type(JsonFieldType.STRING)
                                                .description("반려견 성별: MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED"),
                                        fieldWithPath("data.imageUrl").type(JsonFieldType.STRING)
                                                .description("반려견 이미지 URL")
                                )
                                .requestSchema(Schema.schema("FindPetByIdRequest"))
                                .responseSchema(Schema.schema("FindPetByIdResponse"))
                                .build()))
                );
    }

    @DisplayName("내 반려견 목록 조회 문서화")
    @Test
    void findMine_Success() throws Exception {
        Long loginMemberId = 1L;
        FindPetResponse response1 = new FindPetResponse(
                1L,
                loginMemberId,
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                SizeType.SMALL.toString(),
                Gender.FEMALE.toString(),
                "https://google.com"
        );
        FindPetResponse response2 = new FindPetResponse(
                2L,
                loginMemberId,
                "뚱이",
                "뚱이입니다.",
                LocalDate.now().minusDays(2L),
                SizeType.LARGE.toString(),
                Gender.MALE_NEUTERED.toString(),
                "https://google.com"
        );
        List<FindPetResponse> response = List.of(response1, response2);

        Mockito.when(petQueryService.findByMemberId(any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/pets/mine")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken()))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("pet-findMine-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Pet API")
                                .summary("내 반려견 목록 조회 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 accessToken")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("반려견 id"),
                                        fieldWithPath("data.[].memberId").type(JsonFieldType.NUMBER)
                                                .description("반려견의 주인 id"),
                                        fieldWithPath("data.[].name").type(JsonFieldType.STRING).description("반려견 이름"),
                                        fieldWithPath("data.[].description").type(JsonFieldType.STRING)
                                                .description("반려견 한 줄 소개"),
                                        fieldWithPath("data.[].birthDate").type(JsonFieldType.STRING)
                                                .description("반려견 생년월일: yyyy-MM-dd"),
                                        fieldWithPath("data.[].sizeType").type(JsonFieldType.STRING)
                                                .description("반려견 크기: SMALL, MEDIUM, LARGE"),
                                        fieldWithPath("data.[].gender").type(JsonFieldType.STRING)
                                                .description("반려견 성별: MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED"),
                                        fieldWithPath("data.[].imageUrl").type(JsonFieldType.STRING)
                                                .description("반려견 이미지 URL")
                                )
                                .responseSchema(Schema.schema("FindPetResponse"))
                                .build()))
                );
    }

    @DisplayName("특정 회원의 반려견 목록 조회 문서화")
    @Test
    void findByMemberId_Success() throws Exception {
        Long memberId = 1L;
        FindPetResponse response1 = new FindPetResponse(
                1L,
                memberId,
                "땡이",
                "땡이입니다.",
                LocalDate.now().minusDays(1L),
                SizeType.SMALL.toString(),
                Gender.FEMALE.toString(),
                "https://google.com"
        );
        FindPetResponse response2 = new FindPetResponse(
                2L,
                memberId,
                "뚱이",
                "뚱이입니다.",
                LocalDate.now().minusDays(2L),
                SizeType.LARGE.toString(),
                Gender.MALE_NEUTERED.toString(),
                "https://google.com"
        );
        List<FindPetResponse> response = List.of(response1, response2);

        Mockito.when(petQueryService.findByMemberId(any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/pets")
                        .param("memberId", memberId.toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("pet-findByMemberId-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Pet API")
                                .summary("특정 회원의 반려견 목록 조회 API")
                                .queryParameters(
                                        parameterWithName("memberId").description("조회하려는 회원의 id")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.[].id").type(JsonFieldType.NUMBER).description("반려견 id"),
                                        fieldWithPath("data.[].memberId").type(JsonFieldType.NUMBER)
                                                .description("반려견의 주인 id"),
                                        fieldWithPath("data.[].name").type(JsonFieldType.STRING).description("반려견 이름"),
                                        fieldWithPath("data.[].description").type(JsonFieldType.STRING)
                                                .description("반려견 한 줄 소개"),
                                        fieldWithPath("data.[].birthDate").type(JsonFieldType.STRING)
                                                .description("반려견 생년월일: yyyy-MM-dd"),
                                        fieldWithPath("data.[].sizeType").type(JsonFieldType.STRING)
                                                .description("반려견 크기: SMALL, MEDIUM, LARGE"),
                                        fieldWithPath("data.[].gender").type(JsonFieldType.STRING)
                                                .description("반려견 성별: MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED"),
                                        fieldWithPath("data.[].imageUrl").type(JsonFieldType.STRING)
                                                .description("반려견 이미지 URL")
                                )
                                .responseSchema(Schema.schema("FindPetResponse"))
                                .build()))
                );
    }

    @DisplayName("반려견 정보 업데이트 문서화")
    @Test
    void update_Success() throws Exception {
        // MultipartFile + PATCH Method 같이 사용하기 위한 코드
        MockMultipartHttpServletRequestBuilder patchBuilder
                = RestDocumentationRequestBuilders.multipart("/pets/{id}", 1L);
        patchBuilder.with(request -> {
            request.setMethod(HttpMethod.PATCH.name());
            return request;
        });

        Mockito.doNothing()
                .when(petCommandService)
                .update(any(), any(), any(), any());

        UpdatePetRequest requestDto = new UpdatePetRequest(
                "도토리",
                "도토리 설명",
                LocalDate.now().minusYears(1),
                "SMALL",
                "MALE",
                "UPDATE"
        );

        MockMultipartFile image = new MockMultipartFile(
                "image", "image.jpg", MediaType.MULTIPART_FORM_DATA.toString(), "asdf".getBytes());
        MockMultipartFile request = new MockMultipartFile(
                "request", "request", MediaType.APPLICATION_JSON_VALUE, objectMapper.writeValueAsBytes(requestDto));

        mockMvc.perform(patchBuilder
                        .file(image)
                        .file(request)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getMemberToken()))
                .andExpect(status().isOk())
                .andDo(MockMvcRestDocumentationWrapper.document("pet-update-200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParts(
                                partWithName("image").description("강아지 프로필 이미지 파일"),
                                partWithName("request").description("강아지 등록 정보")
                        ),
                        requestPartFields(
                                "request",
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("반려견 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .description("반려견 한 줄 소개"),
                                fieldWithPath("birthDate").type(JsonFieldType.STRING)
                                        .description("반려견 생년월일: yyyy-MM-dd"),
                                fieldWithPath("sizeType").type(JsonFieldType.STRING)
                                        .description("반려견 크기: SMALL, MEDIUM, LARGE"),
                                fieldWithPath("gender").type(JsonFieldType.STRING)
                                        .description("반려견 성별: MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED"),
                                fieldWithPath("imageUpdateType").type(JsonFieldType.STRING)
                                        .description("이미지 업데이트 여부: UPDATE, NOT_UPDATE, DELETE")
                        ),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Pet API")
                                .summary("""
                                        반려견 정보 수정 API
                                                                                
                                        요청 이미지
                                                                                
                                        multipart/form-data "image" (null인 경우 이미지 변경을 하지 않음)
                                                                                
                                        요청 데이터
                                                                                
                                        application/json "request"
                                                                                
                                        {
                                                                                
                                          "name": "보리", // 변경할 강아지 이름
                                          
                                          "description": "귀여운 보리입니다", // 변경할 강아지 설명
                                          
                                          "birthDate": "2011-01-01", // 강아지 생일 yyyy-MM-dd
                                          
                                          "sizeType": "SMALL", // 변경할 강아지 크기 (SMALL, MEDIUM, LARGE)
                                          
                                          "gender": "MALE", // 변경할 반려견 성별 (MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED)
                                          
                                          "imageUpdateType": "UPDATE" // 이미지 업데이트 여부 -> UPDATE(이미지 변경), NOT_UPDATE(이미지 변경 없음), DELETE(기본 이미지로 변경)
                                                                               
                                        }
                                        """)
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 accessToken")
                                )
                                .pathParameters(
                                        parameterWithName("id").description("수정하려는 Pet ID"))
                                .build()))
                );
    }

    @Override
    protected Object controller() {
        return new PetController(petQueryService, petCommandService);
    }
}
