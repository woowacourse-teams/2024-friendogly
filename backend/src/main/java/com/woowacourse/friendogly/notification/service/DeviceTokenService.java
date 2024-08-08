package com.woowacourse.friendogly.notification.service;

import com.woowacourse.friendogly.member.repository.MemberRepository;
import com.woowacourse.friendogly.notification.domain.DeviceToken;
import com.woowacourse.friendogly.notification.dto.UpdateDeviceTokenRequest;
import com.woowacourse.friendogly.notification.dto.UpdateDeviceTokenResponse;
import com.woowacourse.friendogly.notification.repository.DeviceTokenRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class DeviceTokenService {

    private final DeviceTokenRepository deviceTokenRepository;

    private final MemberRepository memberRepository;

    public DeviceTokenService(DeviceTokenRepository deviceTokenRepository, MemberRepository memberRepository) {
        this.deviceTokenRepository = deviceTokenRepository;
        this.memberRepository = memberRepository;
    }

    public UpdateDeviceTokenResponse update(Long memberId, UpdateDeviceTokenRequest updateDeviceTokenRequest) {
        Optional<DeviceToken> optionalDeviceToken = deviceTokenRepository.findByMemberId(memberId);
        if (optionalDeviceToken.isPresent()) {
            DeviceToken deviceToken = optionalDeviceToken.get();
            deviceToken.changeDeviceToken(updateDeviceTokenRequest.deviceToken());
            return new UpdateDeviceTokenResponse(deviceToken.getDeviceToken());
        }
        DeviceToken deviceToken = deviceTokenRepository.save(
                new DeviceToken(
                        updateDeviceTokenRequest.deviceToken(),
                        memberRepository.getById(memberId)
                )
        );
        return new UpdateDeviceTokenResponse(deviceToken.getDeviceToken());
    }
}
