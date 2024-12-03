package com.happy.friendogly.notification.service;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.notification.domain.NotificationType;
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

    public FcmNotificationService(@Autowired FirebaseApp firebaseApp) {
        this.firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp);
    }

    @Override
    public void sendNotification(
            String title,
            String content,
            NotificationType notificationType,
            List<String> receiverTokens
    ) {
        sendNotification(title, Map.of("body", content), notificationType, receiverTokens);
    }

    @Override
    public void sendNotification(
            String title,
            Map<String, String> contents,
            NotificationType notificationType,
            List<String> receiverTokens
    ) {
        if (!receiverTokens.isEmpty()) {
            MulticastMessage message = MulticastMessage.builder()
                    .putAllData(contents)
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
