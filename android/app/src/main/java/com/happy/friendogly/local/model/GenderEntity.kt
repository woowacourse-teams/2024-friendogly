package com.happy.friendogly.local.model

import kotlinx.serialization.Serializable

@Serializable
enum class GenderEntity {
    MALE,
    FEMALE,
    MALE_NEUTERED,
    FEMALE_NEUTERED,
}
