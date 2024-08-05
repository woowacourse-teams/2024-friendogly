package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.ClubDataSource
import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.model.ClubAddress
import com.happy.friendogly.domain.model.ClubDetail
import com.happy.friendogly.domain.model.ClubFilterCondition
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.domain.repository.ClubRepository
import okhttp3.MultipartBody

class ClubRepositoryImpl(
    private val source: ClubDataSource,
) : ClubRepository {
    override suspend fun postClub(
        title: String,
        content: String,
        address: ClubAddress,
        allowedGender: List<Gender>,
        allowedSize: List<SizeType>,
        memberCapacity: Int,
        file: MultipartBody.Part?,
        petIds: List<Long>,
    ): Result<Unit> =
        source.postClub(
            title = title,
            content = content,
            address = address.toData(),
            allowedGender = allowedGender.map { it.toData() },
            allowedSize = allowedSize.map { it.toData() },
            memberCapacity = memberCapacity,
            file = file,
            petIds = petIds,
        )

    override suspend fun getSearchingClubs(
        filterCondition: ClubFilterCondition,
        address: ClubAddress,
        genderParams: List<Gender>,
        sizeParams: List<SizeType>,
    ): Result<List<Club>> =
        source.getSearchingClubs(
            filterCondition = filterCondition.toData(),
            address = address.toData(),
            genderParams = genderParams.map { it.toData() },
            sizeParams = sizeParams.map { it.toData() },
        ).mapCatching { it.toDomain() }

    override suspend fun getClub(clubId: Long): Result<ClubDetail> = source.getClub(clubId).mapCatching { it.toDomain() }

    override suspend fun postClubMember(
        clubId: Long,
        participatingPetsId: List<Long>,
    ): Result<Unit> =
        source.postClubMember(
            clubId = clubId,
            participatingPetsId = participatingPetsId,
        )

    override suspend fun deleteClubMember(clubId: Long): Result<Unit> = source.deleteClubMember(clubId)
}
