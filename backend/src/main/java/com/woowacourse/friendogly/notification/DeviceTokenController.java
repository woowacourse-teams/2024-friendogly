package com.woowacourse.friendogly.notification;

import com.woowacourse.friendogly.auth.Auth;
import com.woowacourse.friendogly.common.ApiResponse;
import com.woowacourse.friendogly.notification.dto.UpdateDeviceTokenRequest;
import com.woowacourse.friendogly.notification.dto.UpdateDeviceTokenResponse;
import com.woowacourse.friendogly.notification.service.DeviceTokenService;
import com.woowacourse.friendogly.notification.service.FcmNotificationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceTokenController {

    private final DeviceTokenService deviceTokenService;

    public DeviceTokenController(FcmNotificationService fcmNotificationService, DeviceTokenService deviceTokenService) {
        this.deviceTokenService = deviceTokenService;
    }

    @PatchMapping("/walk-status")
    public ApiResponse<UpdateDeviceTokenResponse> updateWalkStatus(
            @Auth Long memberId,
            @Valid @RequestBody UpdateDeviceTokenRequest request
    ) {
        return ApiResponse.ofSuccess(deviceTokenService.update(memberId, request));
    }
}
