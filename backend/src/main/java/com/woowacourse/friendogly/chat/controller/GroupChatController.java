package com.woowacourse.friendogly.chat.controller;

import com.woowacourse.friendogly.auth.WebSocketAuth;
import com.woowacourse.friendogly.chat.dto.request.ChatMessageRequest;
import com.woowacourse.friendogly.chat.dto.response.ChatMessageResponse;
import com.woowacourse.friendogly.chat.service.ChatRoomCommandService;
import com.woowacourse.friendogly.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupChatController {

    private final ChatService chatService;
    private final ChatRoomCommandService chatRoomCommandService;
    private final SimpMessagingTemplate template;

    public GroupChatController(
            ChatService chatService,
            ChatRoomCommandService chatRoomCommandService,
            SimpMessagingTemplate template
    ) {
        this.chatService = chatService;
        this.chatRoomCommandService = chatRoomCommandService;
        this.template = template;
    }

    // TODO: 모임은 chatRoomId를 갖고, 모임에 들어가면 클라이언트가 알아서 이 주소로 publish 하기
    // TODO: 채팅방을 만드는 역할은 ClubService가 하게 하기
    @MessageMapping("/enter/{chatRoomId}")
    public void enter(
            @WebSocketAuth Long memberId,
            @DestinationVariable(value = "chatRoomId") Long chatRoomId
    ) {
        ChatMessageResponse response = chatService.parseEnterMessage(memberId);
        template.convertAndSend("/topic/invite/" + memberId, chatRoomId);
        template.convertAndSend("/topic/chat/" + chatRoomId, response);
    }

    @MessageMapping("/chat/{chatRoomId}")
    public void sendMessage(
            @WebSocketAuth Long memberId,
            @DestinationVariable(value = "chatRoomId") Long chatRoomId,
            @Payload ChatMessageRequest request
    ) {
        ChatMessageResponse response = chatService.parseMessage(memberId, request);
        template.convertAndSend("/topic/chat/" + chatRoomId, response);
    }

    @MessageMapping("/leave/{chatRoomId}")
    public void leave(
            @WebSocketAuth Long memberId,
            @DestinationVariable(value = "chatRoomId") Long chatRoomId
    ) {
        ChatMessageResponse response = chatService.parseLeaveMessage(memberId);
        chatRoomCommandService.leave(memberId, chatRoomId);
        template.convertAndSend("/topic/chat/" + chatRoomId, response);
    }
}
