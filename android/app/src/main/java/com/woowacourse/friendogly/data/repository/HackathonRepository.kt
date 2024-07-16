package com.woowacourse.friendogly.data.repository

import com.kakao.vectormap.LatLng
import com.woowacourse.friendogly.remote.dto.request.RequestPetPostDto
import com.woowacourse.friendogly.remote.dto.response.ResponseFootNearGetDto
import com.woowacourse.friendogly.remote.dto.response.ResponsePetGetDto
import com.woowacourse.friendogly.remote.retrofit.DogRetrofit

object HackathonRepository {

    suspend fun postPet(request: RequestPetPostDto) {
        DogRetrofit.hackathonService.postPets(request)
    }

    suspend fun getPet(memberId: Long): List<ResponsePetGetDto> =
        DogRetrofit.hackathonService.getPets(memberId).body() ?: error("서버 실패")

    suspend fun getFoots(latLng: LatLng): List<ResponseFootNearGetDto> =
        DogRetrofit.hackathonService.getFoots(latLng.latitude, latLng.longitude).body()
            ?: error("서버 실패")

}
