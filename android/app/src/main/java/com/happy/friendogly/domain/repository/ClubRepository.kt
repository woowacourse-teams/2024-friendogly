package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
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
    ): DomainResult<Unit, DataError.Network>

    suspend fun getClub(clubId: Long): DomainResult<ClubDetail, DataError.Network>

    suspend fun postClubMember(
        clubId: Long,
        participatingPetsId: List<Long>,
    ): DomainResult<ClubParticipation, DataError.Network>

    suspend fun deleteClubMember(clubId: Long): DomainResult<Unit, DataError.Network>

    suspend fun patchClub(
        clubId: Long,
        title: String,
        content: String,
        state: ClubState,
    ): DomainResult<Unit, DataError.Network>
}
