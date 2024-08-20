package com.happy.friendogly.chat.controller;

import static com.happy.friendogly.chat.domain.MessageType.ENTER;
import static com.happy.friendogly.chat.domain.MessageType.LEAVE;

import com.happy.friendogly.auth.WebSocketAuth;
import com.happy.friendogly.chat.dto.request.ChatMessageRequest;
import com.happy.friendogly.chat.dto.request.InviteToChatRoomRequest;
import com.happy.friendogly.chat.dto.response.ChatMessageResponse;
import com.happy.friendogly.chat.dto.response.InviteToChatRoomResponse;
import com.happy.friendogly.chat.service.ChatRoomCommandService;
import com.happy.friendogly.chat.service.ChatRoomQueryService;
import com.happy.friendogly.chat.service.ChatService;
import com.happy.friendogly.common.ApiResponse;
import com.happy.friendogly.common.ErrorCode;
import com.happy.friendogly.common.ErrorResponse;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.notification.service.NotificationService;
import java.time.LocalDateTime;
import java.util.Collections;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatSocketController {

    private final ChatService chatService;
    private final ChatRoomCommandService chatRoomCommandService;
    private final ChatRoomQueryService chatRoomQueryService;
    private final SimpMessagingTemplate template;
    private final NotificationService notificationService;

    public ChatSocketController(
            ChatService chatService,
            ChatRoomCommandService chatRoomCommandService,
            ChatRoomQueryService chatRoomQueryService,
            SimpMessagingTemplate template,
            NotificationService notificationService
    ) {
        this.chatService = chatService;
        this.chatRoomCommandService = chatRoomCommandService;
        this.chatRoomQueryService = chatRoomQueryService;
        this.template = template;
        this.notificationService = notificationService;
    }

    @MessageMapping("/invite")
    public void invite(
            @WebSocketAuth Long senderMemberId,
            @Payload InviteToChatRoomRequest request
    ) {
        chatRoomQueryService.validateInvitation(senderMemberId, request);
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
        chatRoomCommandService.enter(memberId, chatRoomId);
        ChatMessageResponse response = chatService.parseNotice(ENTER, memberId, createdAt);

        notificationService.sendChatNotification(chatRoomId, response);
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

        // TODO: 하나의 service method에서 처리하도록 리팩토링 필요
        notificationService.sendChatNotification(chatRoomId, response);
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

        notificationService.sendChatNotification(chatRoomId, response);
        template.convertAndSend("/topic/chat/" + chatRoomId, response);
    }

    @MessageExceptionHandler // TODO: 패키지 정리 과정에서 옮겨야 할 수도 있음!
    public ResponseEntity<ApiResponse<ErrorResponse>> handleException(FriendoglyException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.DEFAULT_ERROR_CODE,
                exception.getMessage(),
                Collections.emptyList()
        );
        return new ResponseEntity<>(ApiResponse.ofError(errorResponse), exception.getHttpStatus());
    }
}
