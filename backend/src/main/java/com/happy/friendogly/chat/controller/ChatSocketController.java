package com.happy.friendogly.chat.controller;

import com.happy.friendogly.auth.WebSocketAuth;
import com.happy.friendogly.chat.dto.request.ChatMessageRequest;
import com.happy.friendogly.chat.dto.request.InviteToChatRoomRequest;
import com.happy.friendogly.chat.dto.response.InviteToChatRoomResponse;
import com.happy.friendogly.chat.service.ChatRoomQueryService;
import com.happy.friendogly.chat.service.ChatCommandService;
import com.happy.friendogly.common.ApiResponse;
import com.happy.friendogly.common.ErrorCode;
import com.happy.friendogly.common.ErrorResponse;
import com.happy.friendogly.exception.FriendoglyException;
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

    private final ChatCommandService chatCommandService;
    private final ChatRoomQueryService chatRoomQueryService;
    private final SimpMessagingTemplate template;

    public ChatSocketController(
            ChatCommandService chatCommandService,
            ChatRoomQueryService chatRoomQueryService,
            SimpMessagingTemplate template
    ) {
        this.chatCommandService = chatCommandService;
        this.chatRoomQueryService = chatRoomQueryService;
        this.template = template;
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
        chatCommandService.enter(memberId, chatRoomId);
    }

    @MessageMapping("/chat/{chatRoomId}")
    public void sendMessage(
            @WebSocketAuth Long memberId,
            @DestinationVariable(value = "chatRoomId") Long chatRoomId,
            @Payload ChatMessageRequest request
    ) {
        chatCommandService.sendChat(memberId, chatRoomId, request);
    }

    @MessageMapping("/leave/{chatRoomId}")
    public void leave(
            @WebSocketAuth Long memberId,
            @DestinationVariable(value = "chatRoomId") Long chatRoomId
    ) {
        chatCommandService.leave(memberId, chatRoomId);
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
