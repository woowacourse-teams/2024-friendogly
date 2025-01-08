package com.happy.friendogly.chatsocket.template;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("rabbit") // RabbitMQ가 필요한 상황에서는 해당 프로파일명을 변경해주세요.
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
