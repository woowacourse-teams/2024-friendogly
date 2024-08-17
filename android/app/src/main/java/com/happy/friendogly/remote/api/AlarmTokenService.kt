package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.request.DeviceTokenRequest
import com.happy.friendogly.remote.model.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.PATCH

interface AlarmTokenService {
    @PATCH(ApiClient.AlarmToken.DEVICE_TOKEN)
    suspend fun patchDeviceTokens(
        @Body body: DeviceTokenRequest,
    ): BaseResponse<Unit>
}