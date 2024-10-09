package com.happy.friendogly.chat.config;

import org.springframework.context.annotation.Profile;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
public class InMemoryChatTemplate implements ChatTemplate {

    private final SimpMessagingTemplate template;

    public InMemoryChatTemplate(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void convertAndSend(String destination, Object payload) {
        template.convertAndSend(destination, payload);
    }
}
