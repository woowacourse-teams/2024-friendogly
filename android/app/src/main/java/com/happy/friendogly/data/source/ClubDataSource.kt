package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.FilterConditionDto
import com.happy.friendogly.data.model.GenderDto
import com.happy.friendogly.data.model.LocationDto
import com.happy.friendogly.data.model.SizeTypeDto

interface ClubDataSource {
    suspend fun postClub(
        title: String,
        content: String,
        address: String,
        allowedGender: List<GenderDto>,
        allowedSize: List<SizeTypeDto>,
        memberCapacity: Int,
        imageUrl: String,
        petIds: List<Long>,
    )

    suspend fun getSearchingClubs(
        filterCondition: FilterConditionDto,
        address: String,
        genderParams: List<GenderDto>,
        sizeParams: List<SizeTypeDto>,
    )

    suspend fun getClub(
        clubId: Long,
    )

    suspend fun postClubMember(
        clubId: Long,
        participatingPetsId: List<Long>,
    )

    suspend fun deleteClubMember(
        clubId: Long,
    )
}
