package com.happy.friendogly.notification.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.happy.friendogly.notification.domain.NotificationType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FcmNotificationServiceTest {

    @InjectMocks
    private FcmNotificationService fcmNotificationService;

    @Mock
    private FirebaseMessaging firebaseMessaging;

    @Mock
    private FirebaseApp firebaseApp;

    @DisplayName("알림을 받을 디바이스가 존재하지 않으면 알림을 보내지 않습니다.")
    @Test
    void sendFootprintNotification() throws FirebaseMessagingException {
        // when
        fcmNotificationService.sendNotification("title", "content", NotificationType.FOOTPRINT, List.of());

        // then
        verify(firebaseMessaging, never()).sendEachForMulticast(any());
    }
}
