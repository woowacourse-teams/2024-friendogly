package com.happy.friendogly.notification.service;

import com.happy.friendogly.chat.dto.response.ChatMessageResponse;
import java.util.List;

public interface NotificationService {

    void sendNotification(String title, String content, String receiverToken);

    void sendNotification(String title, String content, List<String> receiverTokens);

    void sendChat(Long chatRoomId, ChatMessageResponse response);
}
