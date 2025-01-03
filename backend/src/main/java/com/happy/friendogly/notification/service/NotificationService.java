package com.happy.friendogly.notification.service;

import com.happy.friendogly.notification.domain.NotificationType;
import java.util.List;
import java.util.Map;

public interface NotificationService {

    void sendNotification(
            String title,
            String content,
            NotificationType notificationType,
            List<String> receiverTokens
    );

    void sendNotification(
            String title,
            Map<String, String> contents,
            NotificationType notificationType,
            List<String> receiverTokens
    );

    void sendNotificationToTopic(
            String title,
            String content,
            NotificationType notificationType,
            String topic
    );

    void subscribeTopic(List<String> deviceToken, String topic);

    void unsubscribeTopic(List<String> deviceToken, String topic);
}
