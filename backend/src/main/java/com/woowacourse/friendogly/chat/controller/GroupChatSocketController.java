package com.woowacourse.friendogly.chat.controller;

import static com.woowacourse.friendogly.chat.domain.MessageType.*;

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
public class GroupChatSocketController {

    private final ChatService chatService;
    private final ChatRoomCommandService chatRoomCommandService;
    private final SimpMessagingTemplate template;

    public GroupChatSocketController(
            ChatService chatService,
            ChatRoomCommandService chatRoomCommandService,
            SimpMessagingTemplate template
    ) {
        this.chatService = chatService;
        this.chatRoomCommandService = chatRoomCommandService;
        this.template = template;
    }

    @MessageMapping("/enter/{chatRoomId}")
    public void enter(
            @WebSocketAuth Long memberId,
            @DestinationVariable(value = "chatRoomId") Long chatRoomId
    ) {
        ChatMessageResponse response = chatService.parseNotice(ENTER, memberId);
        chatRoomCommandService.enter(memberId, chatRoomId);
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
        ChatMessageResponse response = chatService.parseNotice(LEAVE, memberId);
        chatRoomCommandService.leave(memberId, chatRoomId);
        template.convertAndSend("/topic/chat/" + chatRoomId, response);
    }
}
