package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.ClubAddressDto
import com.happy.friendogly.data.model.ClubDetailDto
import com.happy.friendogly.data.model.ClubDto
import com.happy.friendogly.data.model.FilterConditionDto
import com.happy.friendogly.data.model.GenderDto
import com.happy.friendogly.data.model.LocationDto
import com.happy.friendogly.data.model.SizeTypeDto

interface ClubDataSource {
    suspend fun postClub(
        title: String,
        content: String,
        address: ClubAddressDto,
        allowedGender: List<GenderDto>,
        allowedSize: List<SizeTypeDto>,
        memberCapacity: Int,
        imageUrl: String,
        petIds: List<Long>,
    ): Result<Unit>

    suspend fun getSearchingClubs(
        filterCondition: FilterConditionDto,
        address: String,
        genderParams: List<GenderDto>,
        sizeParams: List<SizeTypeDto>,
    ): Result<List<ClubDto>>

    suspend fun getClub(
        clubId: Long,
    ): Result<ClubDetailDto>

    suspend fun postClubMember(
        clubId: Long,
        participatingPetsId: List<Long>,
    ): Result<Unit>

    suspend fun deleteClubMember(
        clubId: Long,
    ): Result<Unit>
}
