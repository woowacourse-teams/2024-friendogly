package com.happy.friendogly.utils

import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.model.ClubAddress
import com.happy.friendogly.domain.model.ClubState
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.model.UserAddress
import com.happy.friendogly.presentation.ui.club.common.model.ClubItemUiModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDateTime

object TextFixture {
    fun makeClub(): Club {
        return Club(
            id = 0L,
            title = "",
            content = "",
            ownerMemberName = "",
            address = makeClubAddress(),
            status = ClubState.OPEN,
            createdAt = java.time.LocalDateTime.now().toKotlinLocalDateTime(),
            memberCapacity = 5,
            currentMemberCount = 3,
            imageUrl = "",
            petImageUrls = listOf(),
        )
    }

    fun makeUserAddress(): UserAddress {
        return UserAddress("서울특별시",null,null)
    }

    private fun makeClubAddress(): ClubAddress {
        return ClubAddress("서울특별시",null,null)
    }

    fun makePet() : Pet {
        return Pet(
            id = 0,
            memberId = 0,
            name = "",
            description = "",
            birthDate = LocalDate.parse("2024-09-23"),
            sizeType = SizeType.SMALL,
            gender = Gender.MALE_NEUTERED,
            imageUrl =  "",
        )
    }
}
