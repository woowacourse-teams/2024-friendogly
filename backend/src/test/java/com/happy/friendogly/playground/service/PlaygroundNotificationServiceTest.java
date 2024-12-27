package com.happy.friendogly.playground.service;

import static org.mockito.ArgumentMatchers.any;

import com.happy.friendogly.deveicetoken.domain.DeviceToken;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.notification.service.NotificationService;
import com.happy.friendogly.playground.domain.Location;
import com.happy.friendogly.playground.domain.Playground;
import com.happy.friendogly.playground.domain.PlaygroundMember;
import com.happy.friendogly.support.ServiceTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;


class PlaygroundNotificationServiceTest extends ServiceTest{

    @Autowired
    PlaygroundNotificationService playgroundNotificationService;
    @MockBean
    NotificationService notificationService;

    @DisplayName("같은 놀이터의 멤버들은 한번의 구독해제 메서드 호출로 모두 구독해제된다.")
    @Test
    void unsubscribeAllMemberForPlayground() {
        // given
        Mockito.doNothing().when(notificationService).unsubscribeTopic(any(), any());

        Playground playground = playgroundRepository.save(new Playground(new Location(45, 128.121)));
        Member member1 = memberRepository.save(new Member("name", "tag", "imageUrl"));
        Member member2 = memberRepository.save(new Member("name", "tag", "imageUrl"));
        List<PlaygroundMember> playgroundMembers = List.of(
                playgroundMemberRepository.save(new PlaygroundMember(playground, member1)),
                playgroundMemberRepository.save(new PlaygroundMember(playground, member2))
        );
        deviceTokenRepository.save(new DeviceToken(member1,"token1"));
        deviceTokenRepository.save(new DeviceToken(member2,"token2"));

        // when
        playgroundNotificationService.unsubscribeAllMemberForPlayground(playgroundMembers);

        // then
        Mockito.verify(notificationService, Mockito.times(1)).unsubscribeTopic(any(),any());
    }
}