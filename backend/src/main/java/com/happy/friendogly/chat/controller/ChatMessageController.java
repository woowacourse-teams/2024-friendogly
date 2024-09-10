package com.happy.friendogly.chat.controller;

import com.happy.friendogly.auth.Auth;
import com.happy.friendogly.chat.dto.response.FindChatMessagesResponse;
import com.happy.friendogly.chat.service.ChatQueryService;
import com.happy.friendogly.common.ApiResponse;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/{chatRoomId}/recent")
    public ApiResponse<List<FindChatMessagesResponse>> findRecent(
            @Auth Long memberId,
            @RequestParam @PastOrPresent LocalDateTime since,
            @PathVariable("chatRoomId") Long chatRoomId
    ) {
        return ApiResponse.ofSuccess(chatQueryService.findRecent(memberId, chatRoomId, since));
    }
}
