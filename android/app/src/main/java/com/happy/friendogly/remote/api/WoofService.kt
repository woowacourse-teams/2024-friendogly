package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.request.PlaygroundArrivalRequest
import com.happy.friendogly.remote.model.request.PlaygroundRequest
import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.MyPlaygroundResponse
import com.happy.friendogly.remote.model.response.PetExistenceResponse
import com.happy.friendogly.remote.model.response.PlaygroundArrivalResponse
import com.happy.friendogly.remote.model.response.PlaygroundInfoResponse
import com.happy.friendogly.remote.model.response.PlaygroundResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface WoofService {
    @POST(ApiClient.PlayGround.POST_PLAYGROUND)
    suspend fun postPlayground(
        @Body request: PlaygroundRequest,
    ): BaseResponse<MyPlaygroundResponse>

    @PATCH(ApiClient.PlayGround.PATCH_PLAYGROUND_ARRIVAL)
    suspend fun patchPlaygroundArrival(
        @Body request: PlaygroundArrivalRequest,
    ): BaseResponse<PlaygroundArrivalResponse>

    @GET(ApiClient.PlayGround.GET_PLAYGROUNDS)
    suspend fun getPlaygrounds(): BaseResponse<List<PlaygroundResponse>>

    @GET(ApiClient.Pet.GET_PET_EXISTENCE)
    suspend fun getPetExistence(): BaseResponse<PetExistenceResponse>

    @GET(ApiClient.PlayGround.GET_PLAYGROUND_INFO)
    suspend fun getPlaygroundInfo(
        @Path("id") id: Long,
    ): BaseResponse<PlaygroundInfoResponse>

    @DELETE(ApiClient.Footprints.DELETE_FOOTPRINT)
    suspend fun deleteFootprint(
        @Path("footprintId") footprintId: Long,
    ): Response<Unit>
}
