package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.ClubAddressDto
import com.happy.friendogly.data.model.ClubDetailDto
import com.happy.friendogly.data.model.ClubDto
import com.happy.friendogly.data.model.ClubFilterConditionDto
import com.happy.friendogly.data.model.GenderDto
import com.happy.friendogly.data.model.SizeTypeDto
import com.happy.friendogly.data.source.ClubDataSource
import com.happy.friendogly.remote.api.ClubService
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.mapper.toRemote
import com.happy.friendogly.remote.model.request.PostClubMemberRequest
import com.happy.friendogly.remote.model.request.PostClubRequest
import okhttp3.MultipartBody

class ClubDataSourceImpl(private val service: ClubService) : ClubDataSource {
    override suspend fun postClub(
        title: String,
        content: String,
        address: ClubAddressDto,
        allowedGender: List<GenderDto>,
        allowedSize: List<SizeTypeDto>,
        memberCapacity: Int,
        file: MultipartBody.Part?,
        petIds: List<Long>,
    ): Result<Unit> =
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
    ): Result<Unit> =
        runCatching {
            val request = PostClubMemberRequest(participatingPetsId = participatingPetsId)
            service.postClubMember(
                clubId = clubId,
                request = request,
            ).data
        }

    override suspend fun deleteClubMember(clubId: Long): Result<Unit> =
        runCatching {
            service.deleteClubMember(clubId).data
        }
}
