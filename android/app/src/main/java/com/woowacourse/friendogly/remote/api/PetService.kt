package com.woowacourse.friendogly.remote.api

import com.woowacourse.friendogly.remote.model.request.PostPetRequest
import com.woowacourse.friendogly.remote.model.response.BaseResponse
import com.woowacourse.friendogly.remote.model.response.PetResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface PetService {
    @Multipart
    @POST(ApiClient.Pet.POST_PET)
    suspend fun postPet(
        @Part("request") body: PostPetRequest,
        @Part file: MultipartBody.Part?,
    ): BaseResponse<PetResponse>

    @GET(ApiClient.Pet.GET_PETS_MINE)
    suspend fun getPetsMine(): BaseResponse<List<PetResponse>>
}
