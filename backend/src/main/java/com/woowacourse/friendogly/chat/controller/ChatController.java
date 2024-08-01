package com.woowacourse.friendogly.chat.controller;

import com.woowacourse.friendogly.auth.WebSocketAuth;
import com.woowacourse.friendogly.chat.dto.request.ChatMessageRequest;
import com.woowacourse.friendogly.chat.dto.request.InviteToPrivateRoomRequest;
import com.woowacourse.friendogly.chat.dto.response.ChatMessageResponse;
import com.woowacourse.friendogly.chat.dto.response.InvitePrivateChatRoomResponse;
import com.woowacourse.friendogly.chat.service.ChatService;
import com.woowacourse.friendogly.chat.service.PrivateChatRoomCommandService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
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

    @MessageMapping("/private/invite")
    public void inviteToPrivateRoom(
            @WebSocketAuth Long senderMemberId,
            @Payload InviteToPrivateRoomRequest request
    ) {
        InvitePrivateChatRoomResponse response = privateChatRoomCommandService
                .save(senderMemberId, request.receiverMemberId());

        template.convertAndSend("/topic/invite/private/" + senderMemberId, response);
        template.convertAndSend("/topic/invite/private/" + request.receiverMemberId(), response);
        // TODO: 채팅방에 알림 메시지 전달하기
    }

    @MessageMapping("/private/rooms/{chatRoomId}")
    public void sendMessage(
            @WebSocketAuth Long memberId,
            @DestinationVariable(value = "chatRoomId") Long chatRoomId,
            @Payload ChatMessageRequest request
    ) {
        ChatMessageResponse response = chatService.parseMessage(memberId, request);
        template.convertAndSend("/topic/private/" + chatRoomId, response);
    }

    @MessageMapping("/private/leave/{chatRoomId}")
    public void leavePrivateChatRoom(
            @WebSocketAuth Long memberId,
            @DestinationVariable(value = "chatRoomId") Long chatRoomId
    ) {
        privateChatRoomCommandService.leave(memberId, chatRoomId);
        // TODO: 채팅방에 알림 메시지 전달하기
    }

    // TODO: 모든 API에 대해서 공통 응답 만들기
}
