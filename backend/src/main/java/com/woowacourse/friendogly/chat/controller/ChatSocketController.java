package com.woowacourse.friendogly.chat.controller;

import static com.woowacourse.friendogly.chat.domain.MessageType.LEAVE;

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
public class ChatSocketController {

    // TODO: 추후 그룹 채팅방과 1대1 채팅방이 합쳐지면 현재 클래스 삭제하기

    private final ChatService chatService;
    private final PrivateChatRoomCommandService privateChatRoomCommandService;
    private final SimpMessagingTemplate template;

    public ChatSocketController(
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

        template.convertAndSend("/topic/private/invite/" + senderMemberId, response);
        template.convertAndSend("/topic/private/invite/" + request.receiverMemberId(), response);
    }

    @MessageMapping("/private/chat/{chatRoomId}")
    public void sendMessage(
            @WebSocketAuth Long memberId,
            @DestinationVariable(value = "chatRoomId") Long chatRoomId,
            @Payload ChatMessageRequest request
    ) {
        ChatMessageResponse response = chatService.parseMessage(memberId, request);
        template.convertAndSend("/topic/private/chat/" + chatRoomId, response);
    }

    @MessageMapping("/private/leave/{chatRoomId}")
    public void leavePrivateChatRoom(
            @WebSocketAuth Long memberId,
            @DestinationVariable(value = "chatRoomId") Long chatRoomId
    ) {
        ChatMessageResponse response = chatService.parseNotice(LEAVE, memberId);
        privateChatRoomCommandService.leave(memberId, chatRoomId);
        template.convertAndSend("/topic/private/chat/" + chatRoomId, response);
    }
}