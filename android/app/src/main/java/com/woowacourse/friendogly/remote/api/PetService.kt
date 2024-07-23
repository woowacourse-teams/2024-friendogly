package com.woowacourse.friendogly.remote.api

import com.woowacourse.friendogly.remote.model.response.PetResponse
import retrofit2.http.GET

interface PetService {
    @GET(ApiClient.Pet.GET_PETS_MINE)
    suspend fun getPetsMine(): List<PetResponse>
}
