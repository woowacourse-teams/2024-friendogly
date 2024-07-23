package com.woowacourse.friendogly.remote.api

import com.woowacourse.friendogly.remote.model.request.PostPetRequest
import com.woowacourse.friendogly.remote.model.response.BaseResponse
import com.woowacourse.friendogly.remote.model.response.PetResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PetService {
    @POST(ApiClient.Pet.POST_PET)
    suspend fun postPet(
        @Body body: PostPetRequest,
    ): BaseResponse<PetResponse>

    @GET(ApiClient.Pet.GET_PETS_MINE)
    suspend fun getPetsMine(): BaseResponse<List<PetResponse>>
}
