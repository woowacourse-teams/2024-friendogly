package com.happy.friendogly.deveicetoken.service;

import com.happy.friendogly.deveicetoken.domain.DeviceToken;
import com.happy.friendogly.deveicetoken.dto.request.UpdateDeviceTokenRequest;
import com.happy.friendogly.deveicetoken.dto.response.UpdateDeviceTokenResponse;
import com.happy.friendogly.deveicetoken.repository.DeviceTokenRepository;
import com.happy.friendogly.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeviceTokenCommandService {

    private final DeviceTokenRepository deviceTokenRepository;
    private final MemberRepository memberRepository;

    public DeviceTokenCommandService(DeviceTokenRepository deviceTokenRepository, MemberRepository memberRepository) {
        this.deviceTokenRepository = deviceTokenRepository;
        this.memberRepository = memberRepository;
    }

    public UpdateDeviceTokenResponse update(Long memberId, UpdateDeviceTokenRequest updateDeviceTokenRequest) {
        DeviceToken deviceToken = deviceTokenRepository.findByMemberId(memberId)
                .map(token -> {
                    token.updateDeviceToken(updateDeviceTokenRequest.deviceToken());
                    return token;
                })
                .orElseGet(() -> deviceTokenRepository.save(
                        new DeviceToken(memberRepository.getById(memberId),
                                updateDeviceTokenRequest.deviceToken()))
                );
        return new UpdateDeviceTokenResponse(deviceToken.getDeviceToken());
    }
}
