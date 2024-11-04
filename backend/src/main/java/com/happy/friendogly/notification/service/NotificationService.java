package com.happy.friendogly.notification.service;

import com.happy.friendogly.chatsocket.dto.response.ChatMessageSocketResponse;
import com.happy.friendogly.club.domain.Club;
import java.util.List;

public interface NotificationService {

    void sendFootprintNotification(String title, String content, String receiverToken);

    void sendFootprintNotification(String title, String content, List<String> receiverTokens);

    void sendChatNotification(Long chatRoomId, ChatMessageSocketResponse response, Club club);

    void sendPlaygroundJoinNotification(String title, String content, List<String> receiverTokens);
}
