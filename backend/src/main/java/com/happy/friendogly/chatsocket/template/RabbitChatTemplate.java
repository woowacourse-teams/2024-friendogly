package com.happy.friendogly.chatsocket.template;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!local")
public class RabbitChatTemplate implements ChatTemplate {

    private final RabbitTemplate rabbitTemplate;

    public RabbitChatTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void convertAndSend(Long chatRoomId, Object payload) {
        rabbitTemplate.convertAndSend("chat.exchange", "room." + chatRoomId, payload);
    }
}
