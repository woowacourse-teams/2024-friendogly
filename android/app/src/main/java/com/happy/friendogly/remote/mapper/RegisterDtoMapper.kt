package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.RegisterDto
import com.happy.friendogly.remote.model.response.RegisterResponse

fun RegisterResponse.toData(): RegisterDto {
    return RegisterDto(
        id = id,
        name = name,
        tag = tag,
        email = email,
        imageUrl = imageUrl,
        tokens = tokens.toData(),
    )
}
