package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.request.FilterConditionRequest
import com.happy.friendogly.remote.model.request.POSTClubMemberRequest
import com.happy.friendogly.remote.model.request.PostClubRequest
import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.ClubDetailResponse
import com.happy.friendogly.remote.model.response.ClubSearchingResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ClubService {
    @POST(ApiClient.Club.POST_CLUB)
    suspend fun postClub(
        @Body request: PostClubRequest,
    ): BaseResponse<Unit>

    @GET(ApiClient.Club.GET_CLUB_SEARCHING)
    suspend fun getSearchingClubs(
        @Query("filterCondition") filterCondition: FilterConditionRequest,
        @Query("province") province: String,
        @Query("city") city: String,
        @Query("village") village: String,
        @Query("genderParams") genderParams: List<String>,
        @Query("sizeParams") sizeParams: List<String>,
    ): BaseResponse<ClubSearchingResponse>

    @GET(ApiClient.Club.GET_CLUB)
    suspend fun getClub(
        @Path("id") id: Long
    ): BaseResponse<ClubDetailResponse>

    @POST(ApiClient.Club.POST_CLUB_MEMBER)
    suspend fun postClubMember(
        @Path("clubId") clubId: Long,
        @Body request: POSTClubMemberRequest,
    ): BaseResponse<Unit>

    @DELETE(ApiClient.Club.DELETE_CLUB_MEMBER)
    suspend fun deleteClubMember(
        @Path("clubId") clubId: Long,
    ): BaseResponse<Unit>
}
