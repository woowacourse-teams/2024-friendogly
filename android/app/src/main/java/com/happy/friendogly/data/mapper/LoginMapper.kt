package com.happy.friendogly.data.mapper

import com.happy.friendogly.data.model.LoginDto
import com.happy.friendogly.domain.model.Login

fun LoginDto.toDomain(): Login {
    return Login(
        isRegistered = this.isRegistered,
        tokens = this.tokens?.toDomain(),
    )
}
