package com.happy.friendogly.remote.model.request

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
enum class GenderRequest {
    @SerializedName("MALE")
    MALE,

    @SerializedName("FEMALE")
    FEMALE,

    @SerializedName("MALE_NEUTERED")
    MALE_NEUTERED,

    @SerializedName("FEMALE_NEUTERED")
    FEMALE_NEUTERED,
}
