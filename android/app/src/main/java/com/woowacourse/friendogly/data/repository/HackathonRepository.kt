package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.remote.dto.request.RequestFootPostDto
import com.woowacourse.friendogly.remote.dto.request.RequestMemberPostDto
import com.woowacourse.friendogly.remote.dto.request.RequestNearFootGetDto
import com.woowacourse.friendogly.remote.dto.request.RequestPetPostDto
import com.woowacourse.friendogly.remote.dto.request.RequestPetsGetDto
import com.kakao.vectormap.LatLng
import com.woowacourse.friendogly.remote.dto.request.RequestPetPostDto
import com.woowacourse.friendogly.remote.dto.response.ResponseFootNearGetDto
import com.woowacourse.friendogly.remote.dto.response.ResponsePetGetDto
import com.woowacourse.friendogly.remote.retrofit.DogRetrofit

object HackathonRepository {
    suspend fun postPet(request: RequestPetPostDto) =
        runCatching {
            DogRetrofit.hackathonService.postPets(request)
        }

    suspend fun getPet(memberId: Long): Result<List<ResponsePetGetDto>> =
        runCatching {
            DogRetrofit.hackathonService.getPets(memberId).body() ?: error("서버 실패")
        }

    suspend fun getPetByPetId(request: RequestPetsGetDto): Result<ResponsePetGetDto> =
        runCatching {
            DogRetrofit.hackathonService.getPetByPetId(request).body() ?: error("서버 실패")
        }

    suspend fun postMember(request: RequestMemberPostDto): Result<Int> =
        runCatching {
            DogRetrofit.hackathonService.postMember(request).body() ?: error("서버 실패")
            val value = DogRetrofit.hackathonService.postMember(request).headers()["Location"]
            value?.split("/")?.last()?.toInt() ?: 0
        }

    suspend fun postFoot(request: RequestFootPostDto) =
        runCatching {
            DogRetrofit.hackathonService.postFoot(request).body() ?: error("서버 실패")
        }

    suspend fun getFoots(latLng: LatLng): List<ResponseFootNearGetDto> =
        DogRetrofit.hackathonService.getFoots(latLng.latitude, latLng.longitude).body()
            ?: error("서버 실패")
}
