package com.happy.friendogly.notification.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.notification.domain.DeviceToken;
import com.happy.friendogly.notification.dto.request.UpdateDeviceTokenRequest;
import com.happy.friendogly.support.ServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class DeviceTokenCommandServiceTest extends ServiceTest {

    @Autowired
    private DeviceTokenCommandService deviceTokenCommandService;

    private Member member1;

    @BeforeEach
    void setUp() {
        member1 = memberRepository.save(new Member("member1", "tag1", "image1"));
    }

    @DisplayName("디바이스 토큰이 존재할 경우 새로운 디바이스토큰으로 변경된다.")
    @Transactional
    @Test
    void update_changeToNewDeviceToken() {
        // given
        DeviceToken deviceToken = deviceTokenRepository.save(new DeviceToken(member1, "beforeDeviceToken"));

        // when
        deviceTokenCommandService.update(
                member1.getId(),
                new UpdateDeviceTokenRequest("afterDeviceToken")
        );

        // then
        assertThat(deviceToken.getDeviceToken()).isEqualTo("afterDeviceToken");
    }

    @DisplayName("디바이스 토큰이 존재하지 않을 경우 새로운 디바이스토큰이 생성된다.")
    @Test
    void update_createNewDeviceToken() {
        // given
        int beforeSize = deviceTokenRepository.findAll().size();

        // when
        deviceTokenCommandService.update(
                member1.getId(),
                new UpdateDeviceTokenRequest("afterDeviceToken")
        );
        int afterSize = deviceTokenRepository.findAll().size();

        // then
        assertThat(afterSize).isEqualTo(beforeSize + 1);
    }
}
