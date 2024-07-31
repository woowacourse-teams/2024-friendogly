package com.woowacourse.friendogly.docs;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.woowacourse.friendogly.chat.controller.PrivateChatRoomController;
import com.woowacourse.friendogly.chat.dto.response.FindMyPrivateChatRoomResponse;
import com.woowacourse.friendogly.chat.service.PrivateChatRoomQueryService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(PrivateChatRoomController.class)
public class PrivateChatRoomDocsTest extends RestDocsTest {

    @MockBean
    private PrivateChatRoomQueryService privateChatRoomQueryService;

    @DisplayName("자신의 1대1 채팅방 전체 조회")
    @Test
    void findMine() throws Exception {
        List<FindMyPrivateChatRoomResponse> response = List.of(
                new FindMyPrivateChatRoomResponse(2L, "보리맘"),
                new FindMyPrivateChatRoomResponse(5L, "트레이서"),
                new FindMyPrivateChatRoomResponse(11L, "누누파파")
        );

        given(privateChatRoomQueryService.findMine(1L))
                .willReturn(response);

        mockMvc
                .perform(get("/chat-rooms/private")
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, "1"))
                .andDo(print())
                .andDo(document("chat-rooms/private/findMine",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("PrivateChatRoom API")
                                .summary("자신의 1대1 채팅방 전체 조회 API")
                                .requestHeaders(
                                        headerWithName(AUTHORIZATION).description("로그인한 회원 ID")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.[].privateChatRoomId").description("1대1 채팅방 ID"),
                                        fieldWithPath("data.[].oppositeMemberName").description("채팅방 상대의 이름")
                                )
                                .responseSchema(Schema.schema("FindMyPrivateChatRoomResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @Override
    protected Object controller() {
        return new PrivateChatRoomController(privateChatRoomQueryService);
    }
}
