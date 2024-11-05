package com.happy.friendogly.chatsocket.template;

import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class InMemoryChatTemplate implements ChatTemplate {

    private static final String TOPIC_CHAT_PREFIX = "/exchange/chat.exchange/room.";

    private final SimpMessagingTemplate template;

    public InMemoryChatTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void convertAndSend(Long chatRoomId, Object payload) {
        template.convertAndSend(TOPIC_CHAT_PREFIX + chatRoomId, payload);
    }
}
