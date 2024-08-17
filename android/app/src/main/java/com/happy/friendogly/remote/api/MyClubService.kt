package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.ClubResponse
import retrofit2.http.GET

interface MyClubService {

    @GET(ApiClient.MyClub.OWNING)
    suspend fun getMyOwningClubs() : BaseResponse<List<ClubResponse>>

    @GET(ApiClient.MyClub.PARTICIPATING)
    suspend fun getParticipatingClubs() : BaseResponse<List<ClubResponse>>
}
