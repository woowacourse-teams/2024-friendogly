package com.woowacourse.friendogly.chat.controller;

import static com.woowacourse.friendogly.chat.domain.MessageType.ENTER;
import static com.woowacourse.friendogly.chat.domain.MessageType.LEAVE;

import com.woowacourse.friendogly.auth.WebSocketAuth;
import com.woowacourse.friendogly.chat.dto.request.ChatMessageRequest;
import com.woowacourse.friendogly.chat.dto.request.InviteToChatRoomRequest;
import com.woowacourse.friendogly.chat.dto.response.ChatMessageResponse;
import com.woowacourse.friendogly.chat.dto.response.InviteToChatRoomResponse;
import com.woowacourse.friendogly.chat.service.ChatRoomCommandService;
import com.woowacourse.friendogly.chat.service.ChatService;
import java.time.LocalDateTime;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatSocketController {

    private final ChatService chatService;
    private final ChatRoomCommandService chatRoomCommandService;
    private final SimpMessagingTemplate template;

    public ChatSocketController(
            ChatService chatService,
            ChatRoomCommandService chatRoomCommandService,
            SimpMessagingTemplate template
    ) {
        this.chatService = chatService;
        this.chatRoomCommandService = chatRoomCommandService;
        this.template = template;
    }

    @MessageMapping("/invite")
    public void invite(
            @WebSocketAuth Long senderMemberId,
            @Payload InviteToChatRoomRequest request
    ) {
        chatRoomCommandService.invite(senderMemberId, request);
        template.convertAndSend(
                "/topic/invite/" + request.receiverMemberId(),
                new InviteToChatRoomResponse(request.chatRoomId())
        );
    }

    @MessageMapping("/enter/{chatRoomId}")
    public void enter(
            @WebSocketAuth Long memberId,
            @DestinationVariable(value = "chatRoomId") Long chatRoomId
    ) {
        LocalDateTime createdAt = LocalDateTime.now();
        ChatMessageResponse response = chatService.parseNotice(ENTER, memberId, createdAt);
        template.convertAndSend("/topic/chat/" + chatRoomId, response);
    }

    @MessageMapping("/chat/{chatRoomId}")
    public void sendMessage(
            @WebSocketAuth Long memberId,
            @DestinationVariable(value = "chatRoomId") Long chatRoomId,
            @Payload ChatMessageRequest request
    ) {
        LocalDateTime createdAt = LocalDateTime.now();
        ChatMessageResponse response = chatService.parseMessage(memberId, request, createdAt);
        template.convertAndSend("/topic/chat/" + chatRoomId, response);
    }

    @MessageMapping("/leave/{chatRoomId}")
    public void leave(
            @WebSocketAuth Long memberId,
            @DestinationVariable(value = "chatRoomId") Long chatRoomId
    ) {
        LocalDateTime createdAt = LocalDateTime.now();
        chatRoomCommandService.leave(memberId, chatRoomId);
        ChatMessageResponse response = chatService.parseNotice(LEAVE, memberId, createdAt);
        template.convertAndSend("/topic/chat/" + chatRoomId, response);
    }
}
