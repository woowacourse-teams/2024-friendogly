package com.happy.friendogly.docs;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.happy.friendogly.pet.domain.Gender.FEMALE_NEUTERED;
import static com.happy.friendogly.pet.domain.Gender.MALE;
import static com.happy.friendogly.pet.domain.SizeType.MEDIUM;
import static com.happy.friendogly.pet.domain.SizeType.SMALL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.happy.friendogly.chat.controller.ChatRoomController;
import com.happy.friendogly.chat.dto.request.SaveChatRoomRequest;
import com.happy.friendogly.chat.dto.response.ChatRoomDetail;
import com.happy.friendogly.chat.dto.response.FindChatRoomMembersInfoResponse;
import com.happy.friendogly.chat.dto.response.FindClubDetailsResponse;
import com.happy.friendogly.chat.dto.response.FindMyChatRoomResponse;
import com.happy.friendogly.chat.dto.response.SaveChatRoomResponse;
import com.happy.friendogly.chat.service.ChatRoomCommandService;
import com.happy.friendogly.chat.service.ChatRoomQueryService;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;

public class ChatRoomApiDocsTest extends RestDocsTest {

    @Mock
    private ChatRoomQueryService chatRoomQueryService;

    @Mock
    private ChatRoomCommandService chatRoomCommandService;

    @DisplayName("일대일 채팅방 생성")
    @Test
    void savePrivate() throws Exception {
        SaveChatRoomRequest request = new SaveChatRoomRequest(3L);
        SaveChatRoomResponse response = new SaveChatRoomResponse(7L);

        given(chatRoomCommandService.savePrivate(any(), any()))
                .willReturn(response);

        mockMvc
                .perform(post("/chat-rooms")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(APPLICATION_JSON)
                        .header(AUTHORIZATION, getMemberToken()))
                .andDo(print())
                .andDo(document("chat-rooms/savePrivate",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatRoom API")
                                .summary("일대일 채팅방 생성 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .requestFields(
                                        fieldWithPath("otherMemberId").description("채팅방에 초대할 회원의 Member ID")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.chatRoomId").description("생성된 채팅방 ID")
                                )
                                .requestSchema(Schema.schema("SaveChatRoomRequest"))
                                .responseSchema(Schema.schema("SaveChatRoomResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("내가 참여한 채팅방 목록 조회")
    @Test
    void findMine() throws Exception {
        ChatRoomDetail detail1 = new ChatRoomDetail(3L, 5, "신천동 소형견 모임", "https://image1.com/image.jpg");
        ChatRoomDetail detail2 = new ChatRoomDetail(7L, 3, "귀여운 강아지들 모임", "https://image2.com/image.jpg");
        ChatRoomDetail detail3 = new ChatRoomDetail(11L, 6, "멋진 멍멍이 모임", "https://image3.com/image.jpg");
        FindMyChatRoomResponse response = new FindMyChatRoomResponse(1L, List.of(detail1, detail2, detail3));

        given(chatRoomQueryService.findMine(any()))
                .willReturn(response);

        mockMvc
                .perform(get("/chat-rooms/mine")
                        .header(AUTHORIZATION, getMemberToken()))
                .andDo(print())
                .andDo(document("chat-rooms/findMine",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatRoom API")
                                .summary("내가 참여한 채팅방 목록 조회 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.myMemberId").description("나의 Member ID"),
                                        fieldWithPath("data.chatRooms.[].chatRoomId").description("채팅방 ID"),
                                        fieldWithPath("data.chatRooms.[].memberCount").description("채팅방 현재 인원"),
                                        fieldWithPath("data.chatRooms.[].clubName").description("모임 이름"),
                                        fieldWithPath("data.chatRooms.[].clubImageUrl").description("모임 이미지 URL")
                                )
                                .responseSchema(Schema.schema("FindMyChatRoomResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("채팅방 내 Member 목록 조회")
    @Test
    void findMemberInfo() throws Exception {
        FindChatRoomMembersInfoResponse memberInfo1
                = new FindChatRoomMembersInfoResponse(true, 3L, "트레", "https://image1.com/image.jpg");
        FindChatRoomMembersInfoResponse memberInfo2
                = new FindChatRoomMembersInfoResponse(false, 5L, "벼리", "https://image2.com/image.jpg");
        FindChatRoomMembersInfoResponse memberInfo3
                = new FindChatRoomMembersInfoResponse(false, 9L, "땡이", "https://image3.com/image.jpg");

        given(chatRoomQueryService.findMemberInfo(any(), any()))
                .willReturn(List.of(memberInfo1, memberInfo2, memberInfo3));

        mockMvc
                .perform(get("/chat-rooms/{chatRoomId}", 1L)
                        .header(AUTHORIZATION, getMemberToken()))
                .andDo(print())
                .andDo(document("chat-rooms/findMemberInfo",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatRoom API")
                                .summary("채팅방 내 Member 목록 조회 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .pathParameters(
                                        parameterWithName("chatRoomId").description("채팅방 ID")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.[].isOwner").description("채팅방 ID"),
                                        fieldWithPath("data.[].memberId").description("채팅방 현재 인원"),
                                        fieldWithPath("data.[].memberName").description("모임 이름"),
                                        fieldWithPath("data.[].memberProfileImageUrl").description("모임 이미지 URL")
                                )
                                .responseSchema(Schema.schema("FindChatRoomMembersInfoResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @DisplayName("채팅방 내 모임 상세정보 조회")
    @Test
    void findClubDetails() throws Exception {
        FindClubDetailsResponse response
                = new FindClubDetailsResponse(5L, Set.of(SMALL, MEDIUM), Set.of(FEMALE_NEUTERED, MALE));

        given(chatRoomQueryService.findClubDetails(any(), any()))
                .willReturn(response);

        mockMvc
                .perform(get("/chat-rooms/{chatRoomId}/club", 1L)
                        .header(AUTHORIZATION, getMemberToken()))
                .andDo(print())
                .andDo(document("chat-rooms/findClubDetails",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        resource(ResourceSnippetParameters.builder()
                                .tag("ChatRoom API")
                                .summary("채팅방 내 모임 상세정보 조회 API")
                                .requestHeaders(
                                        headerWithName(HttpHeaders.AUTHORIZATION).description("로그인한 회원의 access token")
                                )
                                .pathParameters(
                                        parameterWithName("chatRoomId").description("채팅방 ID")
                                )
                                .responseFields(
                                        fieldWithPath("isSuccess").description("응답 성공 여부"),
                                        fieldWithPath("data.clubId").description("채팅방 ID"),
                                        fieldWithPath("data.allowedSizeTypes")
                                                .description("채팅방 현재 인원 (SMALL, MEDIUM, LARGE)"),
                                        fieldWithPath("data.allowedGenders")
                                                .description("모임 이름 (MALE, FEMALE, MALE_NEUTERED, FEMALE_NEUTERED)")
                                )
                                .responseSchema(Schema.schema("FindClubDetailsResponse"))
                                .build()
                        )
                ))
                .andExpect(status().isOk());
    }

    @Override
    protected Object controller() {
        return new ChatRoomController(chatRoomQueryService, chatRoomCommandService);
    }
}
