package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.request.ClubFilterConditionRequest
import com.happy.friendogly.remote.model.request.PostClubMemberRequest
import com.happy.friendogly.remote.model.request.PostClubRequest
import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.ClubDetailResponse
import com.happy.friendogly.remote.model.response.ClubResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ClubService {
    @Multipart
    @POST(ApiClient.Club.POST_CLUB)
    suspend fun postClub(
        @Part("request") body: PostClubRequest,
        @Part file: MultipartBody.Part?,
    ): BaseResponse<Unit>

    @GET(ApiClient.Club.GET_CLUB_SEARCHING)
    suspend fun getSearchingClubs(
        @Query("filterCondition") filterCondition: ClubFilterConditionRequest,
        @Query("province") province: String,
        @Query("city") city: String,
        @Query("village") village: String,
        @Query("genderParams") genderParams: List<String>,
        @Query("sizeParams") sizeParams: List<String>,
    ): BaseResponse<List<ClubResponse>>

    @GET(ApiClient.Club.GET_CLUB)
    suspend fun getClub(
        @Path("id") id: Long,
    ): BaseResponse<ClubDetailResponse>

    @POST(ApiClient.Club.POST_CLUB_MEMBER)
    suspend fun postClubMember(
        @Path("clubId") clubId: Long,
        @Body request: PostClubMemberRequest,
    ): BaseResponse<Unit>

    @DELETE(ApiClient.Club.DELETE_CLUB_MEMBER)
    suspend fun deleteClubMember(
        @Path("clubId") clubId: Long,
    ): Response<Unit>
}
