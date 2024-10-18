package com.happy.friendogly.chat.config;

public interface ChatTemplate {

    void convertAndSend(Long chatRoomId, Object payload);
}
