package com.happy.friendogly.chat.controller;

import static org.hamcrest.Matchers.contains;

import com.happy.friendogly.chat.domain.MessageType;
import com.happy.friendogly.chat.dto.request.FindMessagesByTimeRangeRequest;
import com.happy.friendogly.chat.dto.response.FindChatMessagesResponse;
import com.happy.friendogly.chat.service.ChatQueryService;
import com.happy.friendogly.support.ControllerTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

class ChatMessageControllerTest extends ControllerTest {

    @MockBean
    private ChatQueryService chatQueryService;

    @DisplayName("두 시점 사이의 채팅 메시지를 조회할 수 있다.")
    @Test
    void name() {
        FindMessagesByTimeRangeRequest request = new FindMessagesByTimeRangeRequest(
                LocalDateTime.parse("2022-01-01T11:00:00.12345"),
                LocalDateTime.parse("2022-01-01T11:02:00.12345")
        );

        Mockito.when(chatQueryService.findByTimeRange(1L, 1L, request))
                .thenReturn(List.of(
                        new FindChatMessagesResponse(
                                MessageType.CHAT,
                                5L,
                                "땡이",
                                "안녕하세요.",
                                LocalDateTime.parse("2022-01-01T11:01:00.12345"),
                                "https://image.com/dy.jpg"
                        ),
                        new FindChatMessagesResponse(
                                MessageType.CHAT,
                                7L,
                                "위브",
                                "네 반가워요.",
                                LocalDateTime.parse("2022-01-01T11:01:30.12345"),
                                "https://image.com/wb.jpg"
                        )
                ));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, getMemberAccessToken(1L))
                .queryParam("since", "2022-01-01T11:00:00.12345")
                .queryParam("until", "2022-01-01T11:02:00.12345")
                .when().get("/chat-messages/1/times")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("data.messageType", contains("CHAT", "CHAT"))
                .body("data.senderMemberId", contains(5, 7))
                .body("data.senderName", contains("땡이", "위브"))
                .body("data.content", contains("안녕하세요.", "네 반가워요."))
                .body("data.createdAt", contains("2022-01-01 11:01:00.123", "2022-01-01 11:01:30.123"))
                .body("data.profilePictureUrl", contains("https://image.com/dy.jpg", "https://image.com/wb.jpg"));
    }
}
