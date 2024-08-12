package com.happy.friendogly.notification.service;

public interface NotificationService {

    void sendNotification(String title, String content, String receiverToken);
}
