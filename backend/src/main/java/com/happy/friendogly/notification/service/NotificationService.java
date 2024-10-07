package com.happy.friendogly.notification.service;

import com.happy.friendogly.chat.dto.response.ChatMessageSocketResponse;
import java.util.List;

public interface NotificationService {

    void sendFootprintNotification(String title, String content, String receiverToken);

    void sendFootprintNotification(String title, String content, List<String> receiverTokens);

    void sendChatNotification(Long chatRoomId, ChatMessageSocketResponse response);
}
