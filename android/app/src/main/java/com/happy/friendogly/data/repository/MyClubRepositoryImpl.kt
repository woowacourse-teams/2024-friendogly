package com.happy.friendogly.data.repository

import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.model.ClubAddress
import com.happy.friendogly.domain.model.ClubState
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.repository.MyClubRepository
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

//TODO: change club api
class MyClubRepositoryImpl : MyClubRepository {
    override suspend fun getMyClubs(): Result<List<Club>> {
        return dummyMyClub
    }

    override suspend fun getMyHeadClubs(): Result<List<Club>> {
        return dummyMyHeadClub
    }
}

private val dummyMyHeadClub = runCatching {
    listOf(
        Club(
            id = -1,
            title = "내가만든클럽~",
            content = "내가 방장인 클럽",
            ownerMemberName = "누누",
            address = ClubAddress(
                "서울특별시",
                "송파구",
                "올림픽로"
            ),
            status = ClubState.OPEN,
            createdAt = java.time.LocalDateTime.now().toKotlinLocalDateTime(),
            allowedSize = listOf(
                SizeType.LARGE,
                SizeType.SMALL,
            ),
            allowedGender = listOf(
                Gender.FEMALE,
                Gender.MALE_NEUTERED,
            ),
            memberCapacity = 5,
            currentMemberCount = 3,
            imageUrl = null,
            petImageUrls = listOf(
                null, null
            )
        )
    )
}

private val dummyMyClub = runCatching {
    listOf(
        Club(
            id = -1,
            title = "내가가입한클럽~",
            content = "내가 가입한 클럽",
            ownerMemberName = "땡이",
            address = ClubAddress(
                "서울특별시",
                "송파구",
                "올림픽로"
            ),
            status = ClubState.OPEN,
            createdAt = LocalDateTime.now().toKotlinLocalDateTime(),
            allowedSize = listOf(
                SizeType.LARGE,
                SizeType.SMALL,
            ),
            allowedGender = listOf(
                Gender.FEMALE,
                Gender.MALE_NEUTERED,
            ),
            memberCapacity = 5,
            currentMemberCount = 3,
            imageUrl = null,
            petImageUrls = listOf(
                null, null
            )
        )
    )
}
