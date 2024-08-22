package com.happy.friendogly.notification.service;

import static com.happy.friendogly.notification.domain.NotificationType.CHAT;
import static com.happy.friendogly.notification.domain.NotificationType.FOOTPRINT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.happy.friendogly.chat.dto.response.ChatMessageResponse;
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
@Profile("!local")
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
    public void sendChatNotification(Long chatRoomId, ChatMessageResponse response) {
        List<String> receiverTokens = deviceTokenRepository
                .findAllByChatRoomIdWithoutMine(chatRoomId, response.senderMemberId());

        if (receiverTokens.isEmpty()) {
            throw new FriendoglyException("기기 토큰이 비어 있어 알림을 전송할 수 없습니다.", INTERNAL_SERVER_ERROR);
        }

        Map<String, String> data = Map.of(
                "chatRoomId", chatRoomId.toString(),
                "messageType", response.messageType().toString(),
                "senderMemberId", response.senderMemberId().toString(),
                "senderName", response.senderName(),
                "content", response.content(),
                "createdAt", response.createdAt().toString(),
                "profilePictureUrl", response.profilePictureUrl()
        );

        sendNotificationWithType(CHAT, "채팅", data, receiverTokens);
    }

    private void sendNotificationWithType(
            NotificationType notificationType,
            String title,
            Map<String, String> data,
            List<String> receiverTokens
    ) {
        if (!receiverTokens.isEmpty()) {
            MulticastMessage message = MulticastMessage.builder()
                    .putAllData(data)
                    .putData("type", notificationType.toString())
                    .putData("title", title)
                    .addAllTokens(receiverTokens)
                    .build();

            try {
                firebaseMessaging.sendEachForMulticast(message);
            } catch (FirebaseMessagingException e) {
                throw new FriendoglyException("FCM을 통해 사용자에게 알림을 보내는 과정에서 에러가 발생했습니다.",
                        INTERNAL_SERVER_ERROR);
            }
        }
    }
}
