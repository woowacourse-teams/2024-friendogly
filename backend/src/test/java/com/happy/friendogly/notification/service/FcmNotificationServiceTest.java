package com.happy.friendogly.notification.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.happy.friendogly.notification.domain.NotificationType;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

    @Disabled // 해당 테스트는 재시도 로직때문에 시간이 오래걸리므로 비활성화 했습니다.
    @DisplayName("RetryableFcmException 예외 발생 시 재시도를 3회 수행한다.")
    @Test
    void retryThirdWhenRetryableFcmException() throws FirebaseMessagingException {
        // given
        Mockito.doThrow(new RetryableFcmException("재시도 가능한 FCM에러 발생", null))
                .when(firebaseMessaging)
                .send(any());

        // when
        fcmNotificationService.sendNotificationToTopic("title", "content", NotificationType.PLAYGROUND, "topic1");

        // then
        verify(firebaseMessaging, times(3)).send(any());
    }
}
