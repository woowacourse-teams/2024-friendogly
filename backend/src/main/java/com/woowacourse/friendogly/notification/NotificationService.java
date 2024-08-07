package com.woowacourse.friendogly.notification;

public interface NotificationService {

    void sendNotification(String title, String content, String receiverToken);
}
