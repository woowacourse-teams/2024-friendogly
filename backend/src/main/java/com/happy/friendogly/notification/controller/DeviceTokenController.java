package com.happy.friendogly.notification.controller;


import com.happy.friendogly.auth.Auth;
import com.happy.friendogly.common.ApiResponse;
import com.happy.friendogly.notification.dto.request.UpdateDeviceTokenRequest;
import com.happy.friendogly.notification.dto.response.UpdateDeviceTokenResponse;
import com.happy.friendogly.notification.service.DeviceTokenCommandService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device-tokens")
public class DeviceTokenController {

    private final DeviceTokenCommandService deviceTokenCommandService;

    public DeviceTokenController(DeviceTokenCommandService deviceTokenCommandService) {
        this.deviceTokenCommandService = deviceTokenCommandService;
    }

    @PutMapping
    public ApiResponse<UpdateDeviceTokenResponse> update(
            @Auth Long memberId,
            @Valid @RequestBody UpdateDeviceTokenRequest request
    ) {
        return ApiResponse.ofSuccess(deviceTokenCommandService.update(memberId, request));
    }
}
