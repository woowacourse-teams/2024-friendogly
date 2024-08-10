package com.happy.friendogly.domain.mapper

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.model.RegisterDto
import com.happy.friendogly.domain.model.Register

fun RegisterDto.toDomain(): Register {
    return Register(
        id = id,
        name = name,
        tag = tag,
        email = email,
        imageUrl = imageUrl,
        tokens = tokens.toDomain(),
    )
}
