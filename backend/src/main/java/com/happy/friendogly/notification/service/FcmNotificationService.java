package com.happy.friendogly.notification.service;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.happy.friendogly.exception.FriendoglyException;
import com.happy.friendogly.notification.domain.NotificationType;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Profile("!local")
public class FcmNotificationService implements NotificationService {

    private final FirebaseMessaging firebaseMessaging;

    public FcmNotificationService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
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
                throw new FriendoglyException("FCM을 통해 사용자에게 알림을 보내는 과정에서 에러가 발생했습니다.", INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    public void sendNotificationToTopic(
            String title,
            String content,
            NotificationType notificationType,
            String topic
    ) {
        Message message = Message.builder()
                .putData("body", content)
                .putData("type", notificationType.toString())
                .putData("title", title)
                .setTopic(topic)
                .build();
        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            throw new FriendoglyException("FCM을 통해 Topic으로 알림을 보내는 과정에서 에러가 발생했습니다.", INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void subscribeTopic(List<String> deviceToken, String topic) {
        try {
            firebaseMessaging.subscribeToTopic(deviceToken, topic);
        } catch (FirebaseMessagingException e) {
            throw new FriendoglyException("FCM을 통해 Topic을 구독하는 과정에서 에러가 발생했습니다.", INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void unsubscribeTopic(List<String> deviceToken, String topic) {
        try {
            firebaseMessaging.unsubscribeFromTopic(deviceToken, topic);
        } catch (FirebaseMessagingException e) {
            throw new FriendoglyException("FCM을 통해 Topic을 구독해제하는 과정에서 에러가 발생했습니다.", INTERNAL_SERVER_ERROR);
        }
    }
}
