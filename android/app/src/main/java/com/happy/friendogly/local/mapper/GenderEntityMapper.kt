package com.happy.friendogly.local.mapper

import com.happy.friendogly.data.model.GenderDto
import com.happy.friendogly.local.model.GenderEntity

fun GenderEntity.toData(): GenderDto {
    return when (this) {
        GenderEntity.MALE -> GenderDto.MALE
        GenderEntity.FEMALE -> GenderDto.FEMALE
        GenderEntity.MALE_NEUTERED -> GenderDto.MALE_NEUTERED
        GenderEntity.FEMALE_NEUTERED -> GenderDto.FEMALE_NEUTERED
    }
}
