package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.LocationResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ClubService {
    @POST(ApiClient.Club.POST_CLUB)
    suspend fun postClub(): BaseResponse<LocationResponse>

    @DELETE(ApiClient.Club.DELETE_CLUB)
    suspend fun deleteClub(
        @Path("id") id: Long,
    ): BaseResponse<LocationResponse>

    @POST(ApiClient.Club.POST_CLUB_PARTICIPATION)
    suspend fun postClubParticipation(): BaseResponse<Unit>

    @GET(ApiClient.Club.GET_CLUB_MINE)
    suspend fun getClubMine(): BaseResponse<Unit>
}
