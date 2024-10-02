package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.request.FootprintRecentWalkStatusAutoRequest
import com.happy.friendogly.remote.model.request.FootprintRecentWalkStatusManualRequest
import com.happy.friendogly.remote.model.request.FootprintRequest
import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.FootprintMarkBtnInfoResponse
import com.happy.friendogly.remote.model.response.FootprintWalkStatusResponse
import com.happy.friendogly.remote.model.response.MyPlaygroundResponse
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
    @POST(ApiClient.Footprints.POST_FOOTPRINT)
    suspend fun postFootprint(
        @Body request: FootprintRequest,
    ): BaseResponse<MyPlaygroundResponse>

    @PATCH(ApiClient.Footprints.PATCH_FOOTPRINT_RECENT_WALK_STATUS_AUTO)
    suspend fun patchFootprintRecentWalkStatusAuto(
        @Body request: FootprintRecentWalkStatusAutoRequest,
    ): BaseResponse<FootprintWalkStatusResponse>

    @PATCH(ApiClient.Footprints.PATCH_FOOTPRINT_RECENT_WALK_STATUS_MANUAL)
    suspend fun patchFootprintRecentWalkStatusManual(
        @Body request: FootprintRecentWalkStatusManualRequest,
    ): BaseResponse<FootprintWalkStatusResponse>

    @GET(ApiClient.PlayGround.GET_PLAYGROUNDS)
    suspend fun getPlaygrounds(): BaseResponse<List<PlaygroundResponse>>

    @GET(ApiClient.Footprints.GET_FOOTPRINT_MINE_LATEST)
    suspend fun getFootprintMarkBtnInfo(): BaseResponse<FootprintMarkBtnInfoResponse>

    @GET(ApiClient.PlayGround.GET_PLAYGROUND_INFO)
    suspend fun getPlaygroundInfo(
        @Path("id") id: Long,
    ): BaseResponse<PlaygroundInfoResponse>

    @DELETE(ApiClient.Footprints.DELETE_FOOTPRINT)
    suspend fun deleteFootprint(
        @Path("footprintId") footprintId: Long,
    ): Response<Unit>
}
