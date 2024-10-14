package com.happy.friendogly.chat.config;

import org.springframework.stereotype.Component;

@Component
public interface ChatTemplate {

    void convertAndSend(Long chatRoomId, Object payload);
}
