package com.happy.friendogly.chat.controller;

import com.happy.friendogly.auth.Auth;
import com.happy.friendogly.chat.dto.response.FindChatMessagesResponse;
import com.happy.friendogly.chat.service.ChatService;
import com.happy.friendogly.common.ApiResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat-messages")
public class ChatMessageController {

    private final ChatService chatService;

    public ChatMessageController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{chatRoomId}")
    public ApiResponse<List<FindChatMessagesResponse>> findAllByChatRoomId(
            @Auth Long memberId,
            @PathVariable("chatRoomId") Long chatRoomId
    ) {
        return ApiResponse.ofSuccess(chatService.findAllByChatRoomId(memberId, chatRoomId));
    }
}
