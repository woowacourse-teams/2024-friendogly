package com.happy.friendogly.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PetExistenceResponse(
    val isExistPet: Boolean,
)
