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
    private MemberCommandService memberCommandService;

    @MockBean
    private MemberQueryService memberQueryService;

    @DisplayName("회원 생성 문서화")
    @Test
    void saveMember_Success() throws Exception {
        SaveMemberRequest request = new SaveMemberRequest("반갑개", "member@email.com");
        SaveMemberResponse response = new SaveMemberResponse(1L, "반갑개", "4e52d416", "member@email.com");

        Mockito.when(memberCommandService.saveMember(request))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/members")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
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
                                        fieldWithPath("isSuccess").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("회원 id"),
                                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("회원 이름"),
                                        fieldWithPath("data.tag").type(JsonFieldType.STRING)
                                                .description("중복된 회원 이름을 식별하기 위한 고유한 문자열"),
                                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("회원 이메일"))
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
