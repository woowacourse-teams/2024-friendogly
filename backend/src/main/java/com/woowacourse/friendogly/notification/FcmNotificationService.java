package com.woowacourse.friendogly.notification;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.woowacourse.friendogly.exception.FriendoglyException;
import org.springframework.stereotype.Service;

@Service
public class FcmNotificationService implements NotificationService {

    private final FirebaseMessaging fcmClient;

    public FcmNotificationService(FirebaseMessaging fcmClient) {
        this.fcmClient = fcmClient;
    }

    @Override
    public void sendNotification(String title, String content, String receiverToken) {
        Message message = Message.builder()
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(title)
                        .setBody(content)
                        .build())
                .setToken(receiverToken)
                .build();
        try {
            fcmClient.send(message, false);
        } catch (FirebaseMessagingException e) {
            throw new FriendoglyException("FCM을 통해 사용자에게 알림을 보내는 과정에서 에러가 발생했습니다.");
        }
    }
}


