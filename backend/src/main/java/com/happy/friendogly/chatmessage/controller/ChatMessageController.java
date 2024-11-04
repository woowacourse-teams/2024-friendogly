package com.happy.friendogly.chatmessage.controller;

import com.happy.friendogly.auth.Auth;
import com.happy.friendogly.chatmessage.dto.request.FindMessagesByTimeRangeRequest;
import com.happy.friendogly.chatmessage.dto.response.FindChatMessagesResponse;
import com.happy.friendogly.chatmessage.service.ChatMessageQueryService;
import com.happy.friendogly.common.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-messages")
public class ChatMessageController {

    private final ChatMessageQueryService chatMessageQueryService;

    public ChatMessageController(ChatMessageQueryService chatMessageQueryService) {
        this.chatMessageQueryService = chatMessageQueryService;
    }

    // TODO: 전체 조회 API 필요성 논의 필요, 만약 필요하다면 페이징을 통한 성능 개선 필요
    @GetMapping("/{chatRoomId}")
    public ApiResponse<List<FindChatMessagesResponse>> findAllByChatRoomId(
            @Auth Long memberId,
            @PathVariable("chatRoomId") Long chatRoomId
    ) {
        return ApiResponse.ofSuccess(chatMessageQueryService.findAllByChatRoomId(memberId, chatRoomId));
    }

    @GetMapping("/{chatRoomId}/times")
    public ApiResponse<List<FindChatMessagesResponse>> findAllByTimeRange(
            @Auth Long memberId,
            @Valid FindMessagesByTimeRangeRequest request,
            @PathVariable("chatRoomId") Long chatRoomId
    ) {
        return ApiResponse.ofSuccess(chatMessageQueryService.findByTimeRange(memberId, chatRoomId, request));
    }
}
