package com.happy.friendogly.docs;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.happy.friendogly.chat.controller.ChatMessageController;
import com.happy.friendogly.chat.domain.MessageType;
import com.happy.friendogly.chat.dto.request.FindMessagesByTimeRangeRequest;
import com.happy.friendogly.chat.dto.response.FindChatMessagesResponse;
import com.happy.friendogly.chat.service.ChatQueryService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;

public class ChatMessageApiDocsTest extends RestDocsTest {

    @Mock
    private ChatQueryService chatQueryService;

    @DisplayName("채팅방의 모든 채팅 내역 조회")
    @Test
    void findAllByChatRoomId() throws Exception {
        List<FindChatMessagesResponse> response = List.of(
                new FindChatMessagesResponse(
                        MessageType.CHAT,
                        3L,
                        "트레",
                        "안녕하세요. 잘 지내시나요?",
                        LocalDateTime.parse("2024-09-01T11:00:00"),
                        "https://picsum.photos/200.jpg"
                ),
                new FindChatMessagesResponse(
                        MessageType.CHAT,
                        5L,
                        "벼리",
                        "네, 잘 지냅니다.",
                        LocalDateTime.parse("2024-09-01T11:10:00"),
                        "https://picsum.photos/300.jpg"
                ),
                new FindChatMessagesResponse(
                        MessageType.CHAT,
                        3L,
                        "트레",
                        "다행이네요.",
                        LocalDateTime.parse("2024-09-01T11:20:00"),
                        "https://picsum.photos/200.jpg"
                ),
                new FindChatMessagesResponse(
                        MessageType.CHAT,
                        5L,
                        "벼리",
                        "그러게요.",
                        LocalDateTime.parse("2024-09-01T11:30:00"),
                        "https://picsum.photos/300.jpg"
                )
        );

        given(chatQueryService.findAllByChatRoomId(anyLong(), anyLong()))
                .willReturn(response);

        mockMvc
                .perform(get("/chat-messages/{chatRoomId}", 1L)
                        .header(AUTHORIZATION, getMemberToken()))
                .andDo(print())
                .andDo(document("chat-messages/findAllByChatRoomId",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatMessage API")
                                .summary("채팅 내역 전체 기간 조회 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .pathParameters(
                                        parameterWithName("chatRoomId").description("조회할 채팅방의 ID")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.[].messageType").description("메시지 타입 (ENTER, CHAT, LEAVE)"),
                                        fieldWithPath("data.[].senderMemberId").description("보낸 사람의 Member ID"),
                                        fieldWithPath("data.[].senderName").description("보낸 사람의 이름"),
                                        fieldWithPath("data.[].content").description("채팅 내용 (ENTER, LEAVE인 경우 빈 문자열)"),
                                        fieldWithPath("data.[].createdAt").description("채팅이 전송된 시간의 LocalDateTime"),
                                        fieldWithPath("data.[].profilePictureUrl").description("보낸 사람의 프로필 사진 URL")
                                )
                                .responseSchema(Schema.schema("FindChatMessagesResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("채팅방의 채팅 내역 기간별 조회")
    @Test
    void findRecent() throws Exception {
        List<FindChatMessagesResponse> response = List.of(
                new FindChatMessagesResponse(
                        MessageType.CHAT,
                        3L,
                        "트레",
                        "다행이네요.",
                        LocalDateTime.parse("2024-09-01T11:20:00"),
                        "https://picsum.photos/200.jpg"
                ),
                new FindChatMessagesResponse(
                        MessageType.CHAT,
                        5L,
                        "벼리",
                        "그러게요.",
                        LocalDateTime.parse("2024-09-01T11:30:00"),
                        "https://picsum.photos/300.jpg"
                )
        );

        given(chatQueryService.findByTimeRange(anyLong(), anyLong(), any(FindMessagesByTimeRangeRequest.class)))
                .willReturn(response);

        mockMvc
                .perform(get("/chat-messages/{chatRoomId}/times", 1L)
                        .header(AUTHORIZATION, getMemberToken())
                        .param("since", "2024-09-01T11:10:00.000")
                        .param("until", "2024-09-01T11:40:00.000"))
                .andDo(print())
                .andDo(document("chat-messages/findAllByTimeRange",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatMessage API")
                                .summary("채팅 내역 특정 기간 조회 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .queryParameters(
                                        parameterWithName("since").description("조회 범위의 시작 시간(LocalDateTime). 해당 시간 바로 후부터 전송된 채팅을 응답한다. (since 시간에 전송된 메시지는 조회 X) (형식 예시: 2024-01-01T20:00:00.12345)"),
                                        parameterWithName("until").description("조회 범위의 종료 시간(LocalDateTime). 해당 시간 바로 전까지 전송된 채팅을 응답한다. (until 시간에 전송된 메시지는 조회 X) (형식 예시: 2024-01-01T20:00:00.12345)")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.[].messageType").description("메시지 타입 (ENTER, CHAT, LEAVE)"),
                                        fieldWithPath("data.[].senderMemberId").description("보낸 사람의 Member ID"),
                                        fieldWithPath("data.[].senderName").description("보낸 사람의 이름"),
                                        fieldWithPath("data.[].content").description("채팅 내용 (ENTER, LEAVE인 경우 빈 문자열)"),
                                        fieldWithPath("data.[].createdAt").description("채팅이 전송된 시간의 LocalDateTime"),
                                        fieldWithPath("data.[].profilePictureUrl").description("보낸 사람의 프로필 사진 URL")
                                )
                                .responseSchema(Schema.schema("FindChatMessagesResponse_Recent"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @Override
    protected Object controller() {
        return new ChatMessageController(chatQueryService);
    }
}
