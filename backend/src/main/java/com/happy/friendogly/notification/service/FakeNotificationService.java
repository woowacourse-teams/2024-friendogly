package com.happy.friendogly.notification.service;

import com.happy.friendogly.chat.dto.response.ChatMessageResponse;
import com.happy.friendogly.notification.domain.NotificationType;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
public class FakeNotificationService implements NotificationService {

    @Override
    public void sendNotification(String title, String content, String receiverToken) {

    }

    @Override
    public void sendNotification(String title, String content, List<String> receiverTokens) {

    }

    @Override
    public void sendNotificationWithType(
            NotificationType notificationType,
            String title,
            Map<String, String> data,
            List<String> receiverTokens
    ) {

    }

    @Override
    public void sendChat(Long chatRoomId, ChatMessageResponse response) {

    }
}
