package com.happy.friendogly.notification.service;

import com.happy.friendogly.chat.dto.response.ChatMessageSocketResponse;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
public class FakeNotificationService implements NotificationService {

    @Override
    public void sendFootprintNotification(String title, String content, String receiverToken) {

    }

    @Override
    public void sendFootprintNotification(String title, String content, List<String> receiverTokens) {

    }

    @Override
    public void sendChatNotification(Long chatRoomId, ChatMessageSocketResponse response) {

    }

    @Override
    public void sendPlaygroundJoinNotification(String title, String content, List<String> receiverTokens) {

    }
}
