package com.happy.friendogly.chat.controller;

import com.happy.friendogly.auth.Auth;
import com.happy.friendogly.chat.dto.request.FindMessagesByTimeRangeRequest;
import com.happy.friendogly.chat.dto.response.FindChatMessagesResponse;
import com.happy.friendogly.chat.service.ChatQueryService;
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

    private final ChatQueryService chatQueryService;

    public ChatMessageController(ChatQueryService chatQueryService) {
        this.chatQueryService = chatQueryService;
    }

    @GetMapping("/{chatRoomId}")
    public ApiResponse<List<FindChatMessagesResponse>> findAllByChatRoomId(
            @Auth Long memberId,
            @PathVariable("chatRoomId") Long chatRoomId
    ) {
        return ApiResponse.ofSuccess(chatQueryService.findAllByChatRoomId(memberId, chatRoomId));
    }

    @GetMapping("/{chatRoomId}/times")
    public ApiResponse<List<FindChatMessagesResponse>> findAllByTimeRange(
            @Auth Long memberId,
            @Valid FindMessagesByTimeRangeRequest request,
            @PathVariable("chatRoomId") Long chatRoomId
    ) {
        return ApiResponse.ofSuccess(chatQueryService.findByTimeRange(memberId, chatRoomId, request));
    }
}
