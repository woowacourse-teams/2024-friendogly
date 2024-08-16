package com.happy.friendogly.notification.service;

import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("local")
public class FakeNotificationService implements NotificationService {

    @Override
    public void sendNotification(String title, String content, String receiverToken) {

    }

    @Override
    public void sendNotification(String title, String content, List<String> receiverTokens) {

    }
}
