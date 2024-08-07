package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.LoginDto
import com.happy.friendogly.remote.model.response.LoginResponse

fun LoginResponse.toData(): LoginDto {
    return LoginDto(
        isRegistered = this.isRegistered,
        tokens = this.tokens?.toData(),
    )
}
