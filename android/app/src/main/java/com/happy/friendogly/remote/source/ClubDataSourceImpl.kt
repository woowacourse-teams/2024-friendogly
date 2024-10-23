package com.happy.friendogly.remote.source

import com.happy.friendogly.data.error.ApiExceptionDto.Companion.ClubSizeExceptionDto
import com.happy.friendogly.data.error.ApiExceptionDto.Companion.FileSizeExceedExceptionDto
import com.happy.friendogly.data.model.ClubAddressDto
import com.happy.friendogly.data.model.ClubDetailDto
import com.happy.friendogly.data.model.ClubDto
import com.happy.friendogly.data.model.ClubFilterConditionDto
import com.happy.friendogly.data.model.ClubParticipationDto
import com.happy.friendogly.data.model.ClubStateDto
import com.happy.friendogly.data.model.GenderDto
import com.happy.friendogly.data.model.SizeTypeDto
import com.happy.friendogly.data.source.ClubDataSource
import com.happy.friendogly.remote.api.ClubService
import com.happy.friendogly.remote.error.ApiExceptionResponse
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.mapper.toRemote
import com.happy.friendogly.remote.model.request.ClubModifyRequest
import com.happy.friendogly.remote.model.request.PostClubMemberRequest
import com.happy.friendogly.remote.model.request.PostClubRequest
import okhttp3.MultipartBody
import javax.inject.Inject

class ClubDataSourceImpl
@Inject
constructor(private val service: ClubService) : ClubDataSource {
    override suspend fun postClub(
        title: String,
        content: String,
        address: ClubAddressDto,
        allowedGender: List<GenderDto>,
        allowedSize: List<SizeTypeDto>,
        memberCapacity: Int,
        file: MultipartBody.Part?,
        petIds: List<Long>,
    ): Result<Unit> {
        val result =
            runCatching {
                val request =
                    PostClubRequest(
                        title = title,
                        content = content,
                        province = address.province,
                        city = address.city,
                        village = address.village,
                        allowedGenders = allowedGender.map { it.toRemote().name },
                        allowedSizes = allowedSize.map { it.toRemote().name },
                        memberCapacity = memberCapacity,
                        participatingPetsId = petIds,
                    )
                service.postClub(
                    body = request,
                    file = file,
                ).data
            }
        return when (val exception = result.exceptionOrNull()) {
            null -> result
            is ApiExceptionResponse -> Result.failure(exception.toData())
            is IllegalStateException -> Result.failure(FileSizeExceedExceptionDto)
            else -> Result.failure(exception)
        }
    }

    override suspend fun getSearchingClubs(
        filterCondition: ClubFilterConditionDto,
        address: ClubAddressDto,
        genderParams: List<GenderDto>,
        sizeParams: List<SizeTypeDto>,
    ): Result<List<ClubDto>> =
        runCatching {
            service.getSearchingClubs(
                filterCondition = filterCondition.toRemote(),
                province = address.province,
                city = address.city,
                village = address.village,
                genderParams = genderParams.map { it.toRemote().name },
                sizeParams = sizeParams.map { it.toRemote().name },
            ).data.toData()
        }

    override suspend fun getClub(clubId: Long): Result<ClubDetailDto> =
        runCatching {
            service.getClub(clubId).data.toData()
        }

    override suspend fun postClubMember(
        clubId: Long,
        participatingPetsId: List<Long>,
    ): Result<ClubParticipationDto> {
        val result = runCatching {
            val request = PostClubMemberRequest(participatingPetsId = participatingPetsId)
            service.postClubMember(
                clubId = clubId,
                request = request,
            ).data.toData()
        }
        return when (val exception = result.exceptionOrNull()) {
            null -> result
            is ApiExceptionResponse -> {
                if (exception.httpCode == 400) {
                    Result.failure(ClubSizeExceptionDto)
                } else {
                    Result.failure(exception.toData())
                }
            }

            else -> Result.failure(exception)
        }
    }

    override suspend fun deleteClubMember(clubId: Long): Result<Unit> =
        runCatching {
            service.deleteClubMember(clubId)
        }

    override suspend fun patchClub(
        clubId: Long,
        title: String,
        content: String,
        state: ClubStateDto,
    ): Result<Unit> {
        val result = runCatching {
            val request =
                ClubModifyRequest(
                    title = title,
                    content = content,
                    status = state.toRemote(),
                )
            service.patchClub(
                clubId = clubId,
                request = request,
            ).data
        }
        return when (val exception = result.exceptionOrNull()) {
            null -> result
            is ApiExceptionResponse -> {
                if (exception.httpCode == 400) {
                    Result.failure(ClubSizeExceptionDto)
                } else {
                    Result.failure(exception.toData())
                }
            }

            else -> Result.failure(exception)
        }
    }
}
