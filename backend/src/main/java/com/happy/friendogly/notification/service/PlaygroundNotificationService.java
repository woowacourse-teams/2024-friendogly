package com.happy.friendogly.notification.service;

import com.happy.friendogly.notification.domain.DeviceToken;
import com.happy.friendogly.notification.domain.NotificationType;
import com.happy.friendogly.notification.repository.DeviceTokenRepository;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PlaygroundNotificationService {

    private static final String DEFAULT_TITLE = "반갑개";

    private final DeviceTokenRepository deviceTokenRepository;
    private final NotificationService notificationService;

    public PlaygroundNotificationService(
            DeviceTokenRepository deviceTokenRepository,
            NotificationService notificationService
    ) {
        this.deviceTokenRepository = deviceTokenRepository;
        this.notificationService = notificationService;
    }

    public void sendJoinNotification(
            String newParticipatingMember,
            List<PlaygroundMember> existingParticipatingMembers
    ) {
        List<String> deviceTokens = toDeviceToken(existingParticipatingMembers);
        String content = newParticipatingMember + "님이 놀이터에 참여했습니다";

        notificationService.sendNotification(
                DEFAULT_TITLE,
                content,
                NotificationType.PLAYGROUND,
                deviceTokens
        );
    }

    private List<String> toDeviceToken(List<PlaygroundMember> playgroundMembers) {
        return playgroundMembers.stream()
                .map(playgroundMember -> deviceTokenRepository.findByMemberId(playgroundMember.getMember().getId()))
                .flatMap(Optional::stream)
                .map(DeviceToken::getDeviceToken)
                .toList();
    }
}
