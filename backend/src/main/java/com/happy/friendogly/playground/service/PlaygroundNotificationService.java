package com.happy.friendogly.playground.service;

import com.happy.friendogly.deveicetoken.domain.DeviceToken;
import com.happy.friendogly.deveicetoken.repository.DeviceTokenRepository;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.notification.domain.NotificationType;
import com.happy.friendogly.notification.service.NotificationService;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class PlaygroundNotificationService {

    private static final String DEFAULT_TITLE = "반갑개";
    private static final String PLAYGROUND_TOPIC_PREFIX = "playground-";

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
            Playground playground
    ) {
        String content = newParticipatingMember + "님이 놀이터에 참여했습니다";

        notificationService.sendNotificationToTopic(
                DEFAULT_TITLE,
                content,
                NotificationType.PLAYGROUND,
                getPlaygroundTopic(playground)
        );
    }

    private List<String> toDeviceToken(List<PlaygroundMember> playgroundMembers) {
        return playgroundMembers.stream()
                .map(playgroundMember -> deviceTokenRepository.findByMemberId(playgroundMember.getMember().getId()))
                .flatMap(Optional::stream)
                .map(DeviceToken::getDeviceToken)
                .toList();
    }

    public void subscribePlaygroundTopic(PlaygroundMember playgroundMember) {
        String deviceToken = deviceTokenRepository.getByMemberId(playgroundMember.getMember().getId()).getDeviceToken();
        String topic = PLAYGROUND_TOPIC_PREFIX + playgroundMember.getPlayground().getId();
        notificationService.subscribeTopic(List.of(deviceToken), topic);
    }

    public void unsubscribePlaygroundTopic(PlaygroundMember playgroundMember) {
        String deviceToken = deviceTokenRepository.getByMemberId(playgroundMember.getMember().getId()).getDeviceToken();
        String topic = PLAYGROUND_TOPIC_PREFIX + playgroundMember.getPlayground().getId();
        notificationService.unsubscribeTopic(List.of(deviceToken), topic);
    }

    public void unsubscribeAllMemberForPlayground(List<PlaygroundMember> playgroundMembers) {
        List<DeviceToken> deviceTokens = getDeviceTokens(playgroundMembers);

        Map<String, List<String>> topicToDeviceTokenMap = getTopicToDeviceTokensMap(deviceTokens, playgroundMembers);

        for (Entry<String, List<String>> topicToDeviceTokenEntity : topicToDeviceTokenMap.entrySet()) {
            notificationService.unsubscribeTopic(topicToDeviceTokenEntity.getValue(), topicToDeviceTokenEntity.getKey());
        }
    }

    private List<DeviceToken> getDeviceTokens(List<PlaygroundMember> playgroundMembers) {
        List<Long> memberIds = playgroundMembers.stream()
                .map(playgroundMember -> playgroundMember.getMember().getId())
                .toList();

        return deviceTokenRepository.findByMemberIdIn(memberIds);
    }

    private Map<String, List<String>> getTopicToDeviceTokensMap(
            List<DeviceToken> deviceTokens,
            List<PlaygroundMember> playgroundMembers
    ) {
        Map<Member, String> memberToDeviceTokenMap = deviceTokens.stream()
                .collect(Collectors.toMap(
                        DeviceToken::getMember,
                        DeviceToken::getDeviceToken
                ));

        return playgroundMembers.stream()
                .collect(Collectors.groupingBy(
                        playgroundMember -> getPlaygroundTopic(playgroundMember.getPlayground()),
                        Collectors.mapping(
                                playgroundMember -> memberToDeviceTokenMap.get(playgroundMember.getMember()),
                                Collectors.toList()
                        )
                ));
    }

    private String getPlaygroundTopic(Playground playground){
        return PLAYGROUND_TOPIC_PREFIX + playground.getId();
    }
}
