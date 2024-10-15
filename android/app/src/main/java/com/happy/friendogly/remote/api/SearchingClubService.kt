package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.request.ClubFilterConditionRequest
import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.SearchingClubResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchingClubService {
    @GET(ApiClient.Club.GET_CLUB_SEARCHING)
    suspend fun getSearchingClubs(
        @Query("filterCondition") filterCondition: ClubFilterConditionRequest,
        @Query("province") province: String,
        @Query("city") city: String?,
        @Query("village") village: String?,
        @Query("genderParams") genderParams: List<String>,
        @Query("sizeParams") sizeParams: List<String>,
        @Query("pageSize") pageSize: String,
        @Query("lastFoundId") lastFoundId: String,
        @Query("lastFoundCreatedAt") lastFoundCreatedAt: String,
    ): BaseResponse<SearchingClubResponse>
}
