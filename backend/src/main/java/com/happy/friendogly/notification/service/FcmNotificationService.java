package com.happy.friendogly.notification.service;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.happy.friendogly.chat.dto.response.ChatMessageResponse;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.notification.domain.NotificationType;
import com.happy.friendogly.notification.repository.DeviceTokenRepository;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Profile("!local")
public class FcmNotificationService implements NotificationService {

    private final FirebaseApp firebaseApp;
    private final DeviceTokenRepository deviceTokenRepository;

    public FcmNotificationService(
            FirebaseApp firebaseApp,
            DeviceTokenRepository deviceTokenRepository
    ) {
        this.firebaseApp = firebaseApp;
        this.deviceTokenRepository = deviceTokenRepository;
    }

    @Override
    public void sendNotification(String title, String content, String receiverToken) {
        Message message = Message.builder()
                .putData("type", NotificationType.FOOTPRINT.toString())
                .putData("title", title)
                .putData("body", content)
                .setToken(receiverToken)
                .build();
        try {
            FirebaseMessaging.getInstance(this.firebaseApp).send(message);
        } catch (FirebaseMessagingException e) {
            throw new FriendoglyException("FCM을 통해 사용자에게 알림을 보내는 과정에서 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void sendNotification(String title, String content, List<String> receiverTokens) {
        MulticastMessage message = MulticastMessage.builder()
                .putData("type", NotificationType.FOOTPRINT.toString())
                .putData("title", title)
                .putData("body", content)
                .addAllTokens(receiverTokens)
                .build();
        try {
            FirebaseMessaging.getInstance(this.firebaseApp).sendEachForMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new FriendoglyException("FCM을 통해 사용자에게 알림을 보내는 과정에서 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void sendChat(Long chatRoomId, ChatMessageResponse response) {
        List<String> receiverTokens = deviceTokenRepository.findAllByChatRoomId(chatRoomId);

        Map<String, String> data = Map.of(
                "chatRoomId", chatRoomId.toString(),
                "messageType", response.messageType().toString(),
                "senderMemberId", response.senderMemberId().toString(),
                "senderName", response.senderName(),
                "content", response.content(),
                "createdAt", response.createdAt().toString(),
                "profilePictureUrl", response.profilePictureUrl()
        );

        sendNotificationWithType(NotificationType.CHAT, "채팅", data, receiverTokens);
    }

    private void sendNotificationWithType(
            NotificationType notificationType,
            String title,
            Map<String, String> data,
            List<String> receiverTokens
    ) {
        MulticastMessage message = MulticastMessage.builder()
                .putAllData(data)
                .putData("type", notificationType.toString())
                .putData("title", title)
                .addAllTokens(receiverTokens)
                .build();

        try {
            FirebaseMessaging.getInstance(this.firebaseApp).sendEachForMulticast(message);
        } catch (FirebaseMessagingException e) {
            throw new FriendoglyException("FCM을 통해 사용자에게 알림을 보내는 과정에서 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
