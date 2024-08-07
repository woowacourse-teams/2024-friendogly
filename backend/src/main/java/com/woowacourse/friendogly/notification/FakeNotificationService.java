package com.woowacourse.friendogly.notification;

import org.springframework.stereotype.Service;

@Service
public class FakeNotificationService implements NotificationService{

    @Override
    public void sendNotification(String title, String content, String receiverToken) {

    }
}
