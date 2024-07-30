package com.woowacourse.friendogly.chat.controller;

import com.woowacourse.friendogly.auth.WebSocketAuth;
import com.woowacourse.friendogly.chat.dto.request.ChatMessageRequest;
import com.woowacourse.friendogly.chat.dto.response.ChatMessageResponse;
import com.woowacourse.friendogly.chat.dto.response.InvitePrivateChatRoomResponse;
import com.woowacourse.friendogly.chat.service.ChatService;
import com.woowacourse.friendogly.chat.service.PrivateChatRoomCommandService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatService chatService;
    private final PrivateChatRoomCommandService privateChatRoomCommandService;
    private final SimpMessagingTemplate template;

    public ChatController(
            ChatService chatService,
            PrivateChatRoomCommandService privateChatRoomCommandService,
            SimpMessagingTemplate template
    ) {
        this.chatService = chatService;
        this.privateChatRoomCommandService = privateChatRoomCommandService;
        this.template = template;
    }

    @MessageMapping("/invite/private/{receiverMemberId}")
    public void inviteToPrivateRoom(
            @WebSocketAuth Long senderMemberId,
            @DestinationVariable(value = "receiverMemberId") Long receiverMemberId
    ) {
        InvitePrivateChatRoomResponse response = privateChatRoomCommandService
                .save(senderMemberId, receiverMemberId);

        template.convertAndSend("/topic/invite/private/" + senderMemberId, response);
        template.convertAndSend("/topic/invite/private/" + receiverMemberId, response);
    }

    @MessageMapping("/rooms/{chatRoomId}")
    @SendTo("/topic/private/{chatRoomId}")
    public ChatMessageResponse sendMessage(
            @WebSocketAuth Long memberId,
            @DestinationVariable(value = "chatRoomId") String chatRoomId,
            @Payload ChatMessageRequest request
    ) {
        return chatService.parseMessage(memberId, request);
    }
}
