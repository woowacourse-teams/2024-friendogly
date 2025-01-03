package com.happy.friendogly.notification.service;

import com.happy.friendogly.notification.domain.NotificationType;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
public class FakeNotificationService implements NotificationService {

    @Override
    public void sendNotification(
            String title,
            String content,
            NotificationType notificationType,
            List<String> receiverTokens
    ) {

    }

    @Override
    public void sendNotification(
            String title,
            Map<String, String> contents,
            NotificationType notificationType,
            List<String> receiverTokens
    ) {

    }

    @Override
    public void sendNotificationToTopic(String title, String content, NotificationType notificationType, String topic) {

    }

    @Override
    public void subscribeTopic(List<String> deviceToken, String topic) {

    }

    @Override
    public void unsubscribeTopic(List<String> deviceToken, String topic) {

    }
}
