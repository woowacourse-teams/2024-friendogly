package com.happy.friendogly.footprint.service;

import com.happy.friendogly.deveicetoken.domain.DeviceToken;
import com.happy.friendogly.footprint.domain.Footprint;
import com.happy.friendogly.footprint.domain.WalkStatus;
import com.happy.friendogly.notification.domain.NotificationType;
import com.happy.friendogly.deveicetoken.repository.DeviceTokenRepository;
import com.happy.friendogly.notification.service.NotificationService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class FootprintNotificationService {

    private static final String DEFAULT_TITLE = "반갑개";

    private final NotificationService notificationService;
    private final DeviceTokenRepository deviceTokenRepository;

    public FootprintNotificationService(
            NotificationService notificationService,
            DeviceTokenRepository deviceTokenRepository
    ) {
        this.notificationService = notificationService;
        this.deviceTokenRepository = deviceTokenRepository;
    }

    public void sendWalkNotificationToMe(Long memberId, WalkStatus currentWalkStatus) {
        String content = null;
        if (currentWalkStatus.isOngoing()) {
            content = "산책을 시작했습니다!";
        }
        if (currentWalkStatus.isAfter()) {
            content = "산책을 종료했습니다!";
        }
        if (currentWalkStatus.isBefore()) {
            return;
        }

        notificationService.sendNotification(
                DEFAULT_TITLE,
                content,
                NotificationType.FOOTPRINT,
                List.of(deviceTokenRepository.getByMemberId(memberId).getDeviceToken())
        );
    }

    public void sendWalkComingNotification(String comingMemberName, List<Footprint> nearFootprints) {
        List<String> nearDeviceTokens = toDeviceToken(nearFootprints);

        notificationService.sendNotification(
                DEFAULT_TITLE,
                "내 산책 장소에 " + comingMemberName + "님도 산책온대요!",
                NotificationType.FOOTPRINT,
                nearDeviceTokens
        );
    }

    public void sendWalkStartNotificationToNear(String startMemberName, List<Footprint> nearFootprints) {
        List<String> nearDeviceTokens = toDeviceToken(nearFootprints);

        notificationService.sendNotification(DEFAULT_TITLE,
                "내 산책장소에 " + startMemberName + "님이 산책을 시작했어요!",
                NotificationType.FOOTPRINT,
                nearDeviceTokens
        );
    }

    private List<String> toDeviceToken(List<Footprint> footprints) {
        return footprints.stream()
                .map(otherFootprint -> deviceTokenRepository.findByMemberId(otherFootprint.getMember().getId()))
                .flatMap(Optional::stream)
                .map(DeviceToken::getDeviceToken)
                .toList();
    }
}
