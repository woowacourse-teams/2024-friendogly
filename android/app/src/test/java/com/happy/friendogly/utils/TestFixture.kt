package com.happy.friendogly.utils

import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.model.ClubAddress
import com.happy.friendogly.domain.model.ClubDetail
import com.happy.friendogly.domain.model.ClubParticipation
import com.happy.friendogly.domain.model.ClubState
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.model.UserAddress
import com.happy.friendogly.presentation.ui.club.common.mapper.toGender
import com.happy.friendogly.presentation.ui.club.common.mapper.toSizeType
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toKotlinLocalDateTime

object TestFixture {
    fun makeClubParticipation(
        memberId: Long = 0L,
        chatRoomId: Long = 0L,
    ): ClubParticipation {
        return ClubParticipation(
            memberId = memberId,
            chatRoomId = chatRoomId,
        )
    }

    fun makeClubDetail(
        clubState: ClubState,
        isMine: Boolean = false,
        alreadyParticipate: Boolean = false,
        canParticipate: Boolean = true,
        isMyPetsEmpty: Boolean = false,
    ): ClubDetail {
        return ClubDetail(
            id = 0L,
            title = "title",
            content = "content",
            ownerMemberName = "ownerMemberName",
            address = makeClubAddress(),
            status = clubState,
            createdAt = java.time.LocalDateTime.now().toKotlinLocalDateTime(),
            memberCapacity = 5,
            currentMemberCount = 3,
            imageUrl = "imageUrl",
            chatRoomId = 0L,
            ownerImageUrl = "ownerImageUrl",
            memberDetails = listOf(),
            allowedSize = ClubFilter.makeSizeFilterEntry().mapNotNull { it.toSizeType() },
            allowedGender = ClubFilter.makeGenderFilterEntry().mapNotNull { it.toGender() },
            petDetails = listOf(),
            isMine = isMine,
            alreadyParticipate = alreadyParticipate,
            canParticipate = canParticipate,
            isMyPetsEmpty = isMyPetsEmpty,
        )
    }

    fun makeClub(): Club {
        return Club(
            id = 0L,
            title = "title",
            content = "content",
            ownerMemberName = "ownerMemberName",
            address = makeClubAddress(),
            status = ClubState.OPEN,
            createdAt = java.time.LocalDateTime.now().toKotlinLocalDateTime(),
            memberCapacity = 5,
            currentMemberCount = 3,
            imageUrl = "imageUrl",
            petImageUrls = listOf(),
        )
    }

    fun makeUserAddress(): UserAddress {
        return UserAddress("서울특별시", null, null)
    }

    private fun makeClubAddress(): ClubAddress {
        return ClubAddress("서울특별시", null, null)
    }

    fun makePet(): Pet {
        return Pet(
            id = 0,
            memberId = 0,
            name = "name",
            description = "description",
            birthDate = LocalDate.parse("2024-09-23"),
            sizeType = SizeType.SMALL,
            gender = Gender.MALE_NEUTERED,
            imageUrl = "imageUrl",
        )
    }
}
