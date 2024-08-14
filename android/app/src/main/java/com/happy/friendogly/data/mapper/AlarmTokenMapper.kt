package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.AlarmTokenDto
import com.happy.friendogly.remote.model.request.DeviceTokenRequest

fun AlarmTokenDto.toRemote(): DeviceTokenRequest =
    DeviceTokenRequest(
        deviceToken = token,
    )
