package com.happy.friendogly.notification.service;

import static com.happy.friendogly.notification.domain.NotificationType.CHAT;
import static com.happy.friendogly.notification.domain.NotificationType.FOOTPRINT;
import static com.happy.friendogly.notification.domain.NotificationType.PLAYGROUND;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.happy.friendogly.chatsocket.dto.response.ChatMessageSocketResponse;
import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.notification.domain.NotificationType;
import com.happy.friendogly.notification.repository.DeviceTokenRepository;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Profile("local")
public class FcmNotificationService implements NotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final DeviceTokenRepository deviceTokenRepository;

    public FcmNotificationService(
            @Autowired FirebaseApp firebaseApp,
            DeviceTokenRepository deviceTokenRepository
    ) {
        this.firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp);
        this.deviceTokenRepository = deviceTokenRepository;
    }

    @Override
    public void sendFootprintNotification(String title, String content, String receiverToken) {
        Map<String, String> data = Map.of(
                "body", content
        );

        sendNotificationWithType(FOOTPRINT, title, data, List.of(receiverToken));
    }

    @Override
    public void sendFootprintNotification(String title, String content, List<String> receiverTokens) {
        Map<String, String> data = Map.of(
                "body", content
        );

        sendNotificationWithType(FOOTPRINT, title, data, receiverTokens);
    }

    @Override
    public void sendChatNotification(Long chatRoomId, ChatMessageSocketResponse response, Club club) {
        List<String> receiverTokens = deviceTokenRepository
                .findAllByChatRoomIdWithoutMine(chatRoomId, response.senderMemberId());

        Map<String, String> data = Map.of(
                "chatRoomId", chatRoomId.toString(),
                "messageType", response.messageType().toString(),
                "senderMemberId", response.senderMemberId().toString(),
                "senderName", response.senderName(),
                "content", response.content(),
                "createdAt", response.createdAt().toString(),
                "profilePictureUrl", response.profilePictureUrl(),
                "clubPictureUrl", club.getImageUrl(),
                "clubTitle", club.getTitle().getValue()
        );

        sendNotificationWithType(CHAT, "채팅", data, receiverTokens);
    }

    @Override
    public void sendPlaygroundJoinNotification(String title, String content, List<String> receiverTokens) {
        Map<String, String> data = Map.of(
                "body", content
        );

        sendNotificationWithType(PLAYGROUND, title, data, receiverTokens);
    }

    @Override
    public void sendPlaygroundJoinNotification(String defaultTitle, String content, String topic) {
        Map<String, String> data = Map.of(
                "body", content
        );

        sendNotificationWithTopic(PLAYGROUND, defaultTitle, data, topic);
    }

    private void sendNotificationWithType(
            NotificationType notificationType,
            String title,
            Map<String, String> data,
            List<String> receiverTokens
    ) {
        if (!receiverTokens.isEmpty()) {
            Message message1 = Message.builder()
                    .setTopic("aa").build();
            MulticastMessage message = MulticastMessage.builder()
                    .putAllData(data)
                    .putData("type", notificationType.toString())
                    .putData("title", title)
                    .addAllTokens(receiverTokens)
                    .build();
            try {
                firebaseMessaging.send(message1);
            } catch (FirebaseMessagingException e) {
                throw new FriendoglyException(e.getMessage()+": "+e.getMessagingErrorCode().toString(),
                        INTERNAL_SERVER_ERROR);
            }
        }
    }

    private void sendNotificationWithTopic(
            NotificationType notificationType,
            String title,
            Map<String, String> data,
            String topic
    ) {
        Message message = Message.builder()
                .setTopic(topic).build();
        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            throw new FriendoglyException(e.getMessage()+": "+e.getMessagingErrorCode().toString(),
                    INTERNAL_SERVER_ERROR);
        }
    }
}
