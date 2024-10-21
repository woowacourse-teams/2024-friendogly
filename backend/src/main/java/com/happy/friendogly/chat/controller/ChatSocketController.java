package com.happy.friendogly.chat.controller;

import com.happy.friendogly.auth.WebSocketAuth;
import com.happy.friendogly.chat.dto.request.ChatMessageSocketRequest;
import com.happy.friendogly.chat.dto.request.InviteToChatRoomRequest;
import com.happy.friendogly.chat.service.ChatCommandService;
import com.happy.friendogly.chat.service.ChatRoomQueryService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatSocketController {

    private final ChatCommandService chatCommandService;
    private final ChatRoomQueryService chatRoomQueryService;

    public ChatSocketController(
            ChatCommandService chatCommandService,
            ChatRoomQueryService chatRoomQueryService
    ) {
        this.chatCommandService = chatCommandService;
        this.chatRoomQueryService = chatRoomQueryService;
    }

    @MessageMapping("/invite") // TODO: 1대1 채팅방 구현 시 수정 필요
    public void invite(
            @WebSocketAuth Long senderMemberId,
            @Payload InviteToChatRoomRequest request
    ) {
        chatRoomQueryService.validateInvitation(senderMemberId, request);
//        template.convertAndSend(
//                "/topic/invite/" + request.receiverMemberId(),
//                new InviteToChatRoomResponse(request.chatRoomId())
//        );
    }

    @MessageMapping("chat.{chatRoomId}")
    public void sendMessage(
            @WebSocketAuth Long memberId,
            @DestinationVariable(value = "chatRoomId") Long chatRoomId,
            @Payload ChatMessageSocketRequest request
    ) {
        chatCommandService.sendChat(memberId, chatRoomId, request);
    }

    @MessageExceptionHandler
    public ResponseEntity<ApiResponse<ErrorResponse>> handleException(FriendoglyException exception) {
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCode.DEFAULT_ERROR_CODE,
                exception.getMessage(),
                Collections.emptyList()
        );
        return new ResponseEntity<>(ApiResponse.ofError(errorResponse), exception.getHttpStatus());
    }
}
