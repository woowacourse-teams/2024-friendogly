package com.woowacourse.friendogly.remote

import com.woowacourse.friendogly.remote.dto.request.RequestFootPostDto
import com.woowacourse.friendogly.remote.dto.request.RequestMemberPostDto
import com.woowacourse.friendogly.remote.dto.request.RequestNearFootGetDto
import com.woowacourse.friendogly.remote.dto.request.RequestPetPostDto
import com.woowacourse.friendogly.remote.dto.request.RequestPetsGetDto
import com.woowacourse.friendogly.remote.dto.response.ResponseFootNearGetDto
import com.woowacourse.friendogly.remote.dto.response.ResponsePetGetDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HackathonService {

    @POST("/pets")
    suspend fun postPets(
        @Body request:RequestPetPostDto
    ):Response<Unit>

    @GET("/pets/mine/{id}")
    suspend fun getPets(
        @Path("id") id:Long,
    ):Response<List<ResponsePetGetDto>>


    @GET("/pets")
    suspend fun getPetByPetId(
        @Body request:RequestPetsGetDto,
    ):Response<ResponsePetGetDto>

    @POST("/member")
    suspend fun postMember(
        @Body request:RequestMemberPostDto,
    ):Response<Unit>

    @POST("/footprints")
    suspend fun postFoot(
        @Body request:RequestFootPostDto
    ):Response<Unit>

    @GET("/footprints/near")
    suspend fun getFoots(
        @Query("lat") latitude:Double,
        @Query("lng") longitude:Double,
    ):Response<List<ResponseFootNearGetDto>>

}
