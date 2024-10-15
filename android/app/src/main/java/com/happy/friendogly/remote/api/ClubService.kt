package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.request.ClubModifyRequest
import com.happy.friendogly.remote.model.request.PostClubMemberRequest
import com.happy.friendogly.remote.model.request.PostClubRequest
import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.ClubDetailResponse
import com.happy.friendogly.remote.model.response.ClubParticipationResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ClubService {
    @Multipart
    @POST(ApiClient.Club.POST_CLUB)
    suspend fun postClub(
        @Part("request") body: PostClubRequest,
        @Part file: MultipartBody.Part?,
    ): BaseResponse<Unit>

    @GET(ApiClient.Club.GET_CLUB)
    suspend fun getClub(
        @Path("id") id: Long,
    ): BaseResponse<ClubDetailResponse>

    @POST(ApiClient.Club.POST_CLUB_MEMBER)
    suspend fun postClubMember(
        @Path("clubId") clubId: Long,
        @Body request: PostClubMemberRequest,
    ): BaseResponse<ClubParticipationResponse>

    @DELETE(ApiClient.Club.DELETE_CLUB_MEMBER)
    suspend fun deleteClubMember(
        @Path("clubId") clubId: Long,
    ): Response<Unit>

    @PATCH(ApiClient.Club.PATCH_CLUB)
    suspend fun patchClub(
        @Path("clubId") clubId: Long,
        @Body request: ClubModifyRequest,
    ): BaseResponse<Unit>
}
