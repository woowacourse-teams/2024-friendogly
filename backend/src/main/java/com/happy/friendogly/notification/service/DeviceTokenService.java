package com.happy.friendogly.notification.service;

import com.happy.friendogly.member.repository.MemberRepository;
import com.happy.friendogly.notification.domain.DeviceToken;
import com.happy.friendogly.notification.dto.request.UpdateDeviceTokenRequest;
import com.happy.friendogly.notification.dto.response.UpdateDeviceTokenResponse;
import com.happy.friendogly.notification.repository.DeviceTokenRepository;
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
            deviceToken.updateDeviceToken(updateDeviceTokenRequest.deviceToken());
            return new UpdateDeviceTokenResponse(deviceToken.getDeviceToken());
        }
        DeviceToken deviceToken = deviceTokenRepository.save(
                new DeviceToken(
                        memberRepository.getById(memberId),
                        updateDeviceTokenRequest.deviceToken()
                        )
        );
        return new UpdateDeviceTokenResponse(deviceToken.getDeviceToken());
    }
}
