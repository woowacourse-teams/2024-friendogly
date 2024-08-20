package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.Club
import com.happy.friendogly.domain.model.ClubAddress
import com.happy.friendogly.domain.model.ClubDetail
import com.happy.friendogly.domain.model.ClubFilterCondition
import com.happy.friendogly.domain.model.ClubParticipation
import com.happy.friendogly.domain.model.ClubState
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import okhttp3.MultipartBody

interface ClubRepository {
    suspend fun postClub(
        title: String,
        content: String,
        address: ClubAddress,
        allowedGender: List<Gender>,
        allowedSize: List<SizeType>,
        memberCapacity: Int,
        file: MultipartBody.Part?,
        petIds: List<Long>,
    ): Result<Unit>

    suspend fun getSearchingClubs(
        filterCondition: ClubFilterCondition,
        address: ClubAddress,
        genderParams: List<Gender>,
        sizeParams: List<SizeType>,
    ): Result<List<Club>>

    suspend fun getClub(clubId: Long): Result<ClubDetail>

    suspend fun postClubMember(
        clubId: Long,
        participatingPetsId: List<Long>,
    ): Result<ClubParticipation>

    suspend fun deleteClubMember(clubId: Long): Result<Unit>

    suspend fun patchClub(
        clubId: Long,
        title: String,
        content: String,
        state: ClubState,
    ): Result<Unit>
}
