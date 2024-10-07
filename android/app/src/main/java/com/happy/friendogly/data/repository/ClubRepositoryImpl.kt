package com.happy.friendogly.data.repository

import com.happy.friendogly.data.error.toDomainError
import com.happy.friendogly.data.mapper.toData
import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.ClubDataSource
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
import com.happy.friendogly.domain.repository.ClubRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class ClubRepositoryImpl
    @Inject
    constructor(
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
        ): DomainResult<Unit, DataError.Network> {
            return source.postClub(
                title = title,
                content = content,
                address = address.toData(),
                allowedGender = allowedGender.map { it.toData() },
                allowedSize = allowedSize.map { it.toData() },
                memberCapacity = memberCapacity,
                file = file,
                petIds = petIds,
            ).fold(
                onSuccess = { result ->
                    DomainResult.Success(result)
                },
                onFailure = { error ->
                    error.toDomainError()
                },
            )
        }

        override suspend fun getSearchingClubs(
            filterCondition: ClubFilterCondition,
            address: ClubAddress,
            genderParams: List<Gender>,
            sizeParams: List<SizeType>,
        ): DomainResult<List<Club>, DataError.Network> {
            return source.getSearchingClubs(
                filterCondition = filterCondition.toData(),
                address = address.toData(),
                genderParams = genderParams.map { it.toData() },
                sizeParams = sizeParams.map { it.toData() },
            ).fold(
                onSuccess = { clubs ->
                    DomainResult.Success(clubs.toDomain())
                },
                onFailure = { error ->
                    error.toDomainError()
                },
            )
        }

        override suspend fun getClub(clubId: Long): DomainResult<ClubDetail, DataError.Network> {
            return source.getClub(clubId).fold(
                onSuccess = { clubDetail ->
                    DomainResult.Success(clubDetail.toDomain())
                },
                onFailure = { error ->
                    error.toDomainError()
                },
            )
        }

        override suspend fun postClubMember(
            clubId: Long,
            participatingPetsId: List<Long>,
        ): DomainResult<ClubParticipation, DataError.Network> {
            return source.postClubMember(
                clubId = clubId,
                participatingPetsId = participatingPetsId,
            ).fold(
                onSuccess = { clubParticipation ->
                    DomainResult.Success(clubParticipation.toDomain())
                },
                onFailure = { error ->
                    error.toDomainError()
                },
            )
        }

        override suspend fun deleteClubMember(clubId: Long): DomainResult<Unit, DataError.Network> {
            return source.deleteClubMember(clubId).fold(
                onSuccess = { result ->
                    DomainResult.Success(result)
                },
                onFailure = { error ->
                    error.toDomainError()
                },
            )
        }

        override suspend fun patchClub(
            clubId: Long,
            title: String,
            content: String,
            state: ClubState,
        ): DomainResult<Unit, DataError.Network> {
            return source.patchClub(
                clubId = clubId,
                title = title,
                content = content,
                state = state.toData(),
            ).fold(
                onSuccess = { result ->
                    DomainResult.Success(result)
                },
                onFailure = { error ->
                    error.toDomainError()
                },
            )
        }
    }
