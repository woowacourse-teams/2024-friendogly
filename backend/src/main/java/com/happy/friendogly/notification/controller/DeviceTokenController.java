package com.happy.friendogly.notification.controller;


import com.happy.friendogly.auth.Auth;
import com.happy.friendogly.common.ApiResponse;
import com.happy.friendogly.notification.dto.request.UpdateDeviceTokenRequest;
import com.happy.friendogly.notification.dto.response.UpdateDeviceTokenResponse;
import com.happy.friendogly.notification.service.DeviceTokenService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device-tokens")
public class DeviceTokenController {

    private final DeviceTokenService deviceTokenService;

    public DeviceTokenController(DeviceTokenService deviceTokenService) {
        this.deviceTokenService = deviceTokenService;
    }

    @PatchMapping
    public ApiResponse<UpdateDeviceTokenResponse> update(
            @Auth Long memberId,
            @Valid @RequestBody UpdateDeviceTokenRequest request
    ) {
        return ApiResponse.ofSuccess(deviceTokenService.update(memberId, request));
    }
}
