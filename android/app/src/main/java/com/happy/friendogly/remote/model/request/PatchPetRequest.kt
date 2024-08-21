package com.happy.friendogly.remote.model.request

import com.happy.friendogly.remote.util.LocalDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PatchPetRequest(
    val name: String,
    val description: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthDate: LocalDate,
    val sizeType: SizeTypeRequest,
    val gender: GenderRequest,
    val imageUpdateType: ImageUpdateTypeRequest,
)
