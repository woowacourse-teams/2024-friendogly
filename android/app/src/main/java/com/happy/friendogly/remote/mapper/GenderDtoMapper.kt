package com.happy.friendogly.remote.mapper

import com.happy.friendogly.data.model.GenderDto
import com.happy.friendogly.remote.model.request.GenderRequest
import com.happy.friendogly.remote.model.response.GenderResponse

fun GenderResponse.toData(): GenderDto {
    return when (this) {
        GenderResponse.MALE -> GenderDto.MALE
        GenderResponse.FEMALE -> GenderDto.FEMALE
        GenderResponse.MALE_NEUTERED -> GenderDto.MALE_NEUTERED
        GenderResponse.FEMALE_NEUTERED -> GenderDto.FEMALE_NEUTERED
    }
}

fun GenderDto.toRemote(): GenderRequest {
    return when (this) {
        GenderDto.MALE -> GenderRequest.MALE
        GenderDto.FEMALE -> GenderRequest.FEMALE
        GenderDto.MALE_NEUTERED -> GenderRequest.MALE_NEUTERED
        GenderDto.FEMALE_NEUTERED -> GenderRequest.FEMALE_NEUTERED
    }
}
