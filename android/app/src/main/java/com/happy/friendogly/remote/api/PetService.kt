package com.happy.friendogly.remote.api

import com.happy.friendogly.remote.model.request.PostPetRequest
import com.happy.friendogly.remote.model.response.BaseResponse
import com.happy.friendogly.remote.model.response.PetResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface PetService {
    @Multipart
    @POST(ApiClient.Pet.POST_PET)
    suspend fun postPet(
        @Part("request") body: PostPetRequest,
        @Part file: MultipartBody.Part?,
    ): BaseResponse<PetResponse>

    @GET(ApiClient.Pet.GET_PETS_MINE)
    suspend fun getPetsMine(): BaseResponse<List<PetResponse>>

    @GET(ApiClient.Pet.GET_PETS)
    suspend fun getPets(
        @Query("memberId") id: Long,
    ): BaseResponse<List<PetResponse>>
}
