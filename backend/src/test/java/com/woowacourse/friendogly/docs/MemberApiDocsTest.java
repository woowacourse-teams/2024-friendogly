package com.woowacourse.friendogly.docs;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.woowacourse.friendogly.member.controller.MemberController;
import com.woowacourse.friendogly.member.dto.request.SaveMemberRequest;
import com.woowacourse.friendogly.member.dto.response.SaveMemberResponse;
import com.woowacourse.friendogly.member.service.MemberCommandService;
import com.woowacourse.friendogly.member.service.MemberQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;

@WebMvcTest(MemberController.class)
public class MemberApiDocsTest extends RestDocsTest {

    @MockBean
    MemberCommandService memberCommandService;

    @MockBean
    MemberQueryService memberQueryService;

    @DisplayName("회원 생성 문서화 테스트 예시")
    @Test
    void saveMember_Success() throws Exception {
        SaveMemberRequest request = new SaveMemberRequest("반갑개", "member@email.com");
        SaveMemberResponse response = new SaveMemberResponse(1L, "반갑개", "member@email.com");

        Mockito.when(memberCommandService.saveMember(request))
                .thenReturn(response);
        // http method() static import 가능, 단 라이브러리 확인 필수
        mockMvc.perform(RestDocumentationRequestBuilders.post("/members")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                //*** document() static import 가능함, 단 라이브러리 확인 필수
                .andDo(MockMvcRestDocumentationWrapper.document("member-save-201",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("회원 생성 API")
                                .requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"))
                                .responseFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("회원 id"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"))
                                .requestSchema(Schema.schema("saveMemberRequest"))
                                .responseSchema(Schema.schema("응답DTO 이름"))
                                .build()))
                );
    }

    @Override
    protected Object controller() {
        return new MemberController(memberCommandService);
    }
}
