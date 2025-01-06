package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.PlaygroundMessageResponse
import com.happy.friendogly.remote.model.request.PatchPlaygroundArrivalRequest
import com.happy.friendogly.remote.model.request.PatchPlaygroundMessageRequest
import com.happy.friendogly.remote.model.request.PostPlaygroundRequest
import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.MyPlaygroundResponse
import com.happy.friendogly.remote.model.response.PetExistenceResponse
import com.happy.friendogly.remote.model.response.PlaygroundArrivalResponse
import com.happy.friendogly.remote.model.response.PlaygroundInfoResponse
import com.happy.friendogly.remote.model.response.PlaygroundJoinResponse
import com.happy.friendogly.remote.model.response.PlaygroundResponse
import com.happy.friendogly.remote.model.response.PlaygroundSummaryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaygroundService {
    @POST(ApiClient.PlayGround.POST_PLAYGROUND)
    suspend fun postPlayground(
        @Body request: PostPlaygroundRequest,
    ): BaseResponse<MyPlaygroundResponse>

    @PATCH(ApiClient.PlayGround.PATCH_PLAYGROUND_ARRIVAL)
    suspend fun patchPlaygroundArrival(
        @Body request: PatchPlaygroundArrivalRequest,
    ): BaseResponse<PlaygroundArrivalResponse>

    @GET(ApiClient.PlayGround.GET_PLAYGROUNDS)
    suspend fun getPlaygrounds(
        @Query("startLatitude") startLatitude: Double,
        @Query("endLatitude") endLatitude: Double,
        @Query("startLongitude") startLongitude: Double,
        @Query("endLongitude") endLongitude: Double,
    ): BaseResponse<List<PlaygroundResponse>>

    @GET(ApiClient.PlayGround.GET_MY_PLAYGROUND)
    suspend fun getMyPlayground(): BaseResponse<MyPlaygroundResponse>

    @GET(ApiClient.Pet.GET_PET_EXISTENCE)
    suspend fun getPetExistence(): BaseResponse<PetExistenceResponse>

    @GET(ApiClient.PlayGround.GET_PLAYGROUND_INFO)
    suspend fun getPlaygroundInfo(
        @Path("id") id: Long,
    ): BaseResponse<PlaygroundInfoResponse>

    @GET(ApiClient.PlayGround.GET_PLAYGROUND_SUMMARY)
    suspend fun getPlaygroundSummary(
        @Path("playgroundId") playgroundId: Long,
    ): BaseResponse<PlaygroundSummaryResponse>

    @POST(ApiClient.PlayGround.POST_PLAYGROUND_JOIN)
    suspend fun postPlaygroundJoin(
        @Path("playgroundId") playgroundId: Long,
    ): BaseResponse<PlaygroundJoinResponse>

    @DELETE(ApiClient.PlayGround.DELETE_PLAYGROUND_LEAVE)
    suspend fun deletePlaygroundLeave(): Response<Unit>

    @PATCH(ApiClient.PlayGround.PATCH_PLAYGROUND_MESSAGE)
    suspend fun patchPlaygroundMessage(
        @Body request: PatchPlaygroundMessageRequest,
    ): BaseResponse<PlaygroundMessageResponse>
}
