package com.happy.friendogly.notification.service;

import java.util.List;

public interface NotificationService {

    void sendNotification(String title, String content, String receiverToken);

    void sendNotification(String title, String content, List<String> receiverToken);
}
