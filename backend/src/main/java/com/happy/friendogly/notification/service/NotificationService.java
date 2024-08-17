package com.happy.friendogly.notification.service;

import com.happy.friendogly.chat.dto.response.ChatMessageResponse;
import com.happy.friendogly.notification.domain.NotificationType;
import java.util.List;
import java.util.Map;

public interface NotificationService {

    void sendNotification(String title, String content, String receiverToken);

    void sendNotification(String title, String content, List<String> receiverTokens);

    void sendNotificationWithType(
            NotificationType notificationType,
            Map<String, String> data,
            List<String> receiverTokens
    );

    void sendChat(Long chatRoomId, ChatMessageResponse response);
}
