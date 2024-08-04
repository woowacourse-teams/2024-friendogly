package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.request.POSTClubMemberRequest
import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.LocationResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ClubService {
    @POST(ApiClient.Club.POST_CLUB)
    suspend fun postClub(): BaseResponse<LocationResponse>

    @GET(ApiClient.Club.GET_CLUB_SEARCHING)
    suspend fun getSearchingClubs(
        @Query("filterCondition") filterCondition: String,
        @Query("address") address: String,
        @Query("genderParams") genderParams: List<String>,
        @Query("sizeParams") sizeParams: List<String>,
    )

    @GET(ApiClient.Club.GET_CLUB)
    suspend fun getClub(
        @Path("id") id: Long
    )

    @POST(ApiClient.Club.POST_CLUB_MEMBER)
    suspend fun postClubMember(
        @Path("clubId") clubId: Long,
        @Body request: POSTClubMemberRequest,
    )

    @DELETE(ApiClient.Club.DELETE_CLUB_MEMBER)
    suspend fun deleteClubMember(
        @Path("clubId") clubId: Long,
    )
}
