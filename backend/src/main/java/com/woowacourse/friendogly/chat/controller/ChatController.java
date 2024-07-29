package com.woowacourse.friendogly.chat.controller;

import com.woowacourse.friendogly.auth.WebSocketAuth;
import com.woowacourse.friendogly.chat.dto.ChatMessageRequest;
import com.woowacourse.friendogly.chat.dto.ChatMessageResponse;
import com.woowacourse.friendogly.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/rooms/{chatRoomId}")
    @SendTo("/topic/{chatRoomId}")
    public ChatMessageResponse sendMessage(
            @WebSocketAuth Long memberId,
            @DestinationVariable String chatRoomId,
            @Payload ChatMessageRequest request
    ) {
        return chatService.parseData(memberId, request);
    }
}
