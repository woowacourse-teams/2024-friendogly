package com.happy.friendogly.chatsocket.template;

public interface ChatTemplate {

    void convertAndSend(Long chatRoomId, Object payload);
}
