package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.FootprintInfoDto
import com.happy.friendogly.data.source.FootprintDataSource
import com.happy.friendogly.remote.api.FootprintService
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.model.response.FootprintInfoResponse
import com.happy.friendogly.remote.model.response.GenderResponse
import com.happy.friendogly.remote.model.response.PetDetailResponse
import com.happy.friendogly.remote.model.response.SizeTypeResponse
import com.happy.friendogly.remote.model.response.WalkStatusResponse
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class FootprintDataSourceImpl(private val service: FootprintService) :
    FootprintDataSource {
    override suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfoDto> {
        return runCatching {
            FootprintInfoResponse(
                memberName = "John Doe",
                walkStatus = WalkStatusResponse.ONGOING,
                changedWalkStatusTime = LocalDateTime(2024, 7, 30, 21, 0, 0),
                pets =
                    listOf(
                        PetDetailResponse(
                            name = "Buddy1",
                            description = "Buddy is a friendly dog who loves to play and meet new people.",
                            birthDate = LocalDate(2015, 5, 20),
                            sizeType = SizeTypeResponse.SMALL,
                            gender = GenderResponse.MALE,
                            imageUrl = "https://shop.peopet.co.kr/data/goods/388/2022/06/_temp_16557127733930view.jpg",
                        ),
                        PetDetailResponse(
                            name = "Buddy2",
                            description = "Buddy is a friendly dog who loves to play and meet new people.",
                            birthDate = LocalDate(2015, 5, 20),
                            sizeType = SizeTypeResponse.SMALL,
                            gender = GenderResponse.MALE,
                            imageUrl = "https://shop.peopet.co.kr/data/goods/388/2022/06/_temp_16557127733930view.jpg",
                        ),
                        PetDetailResponse(
                            name = "Buddy3",
                            description = "Buddy is a friendly dog who loves to play and meet new people.",
                            birthDate = LocalDate(2015, 5, 20),
                            sizeType = SizeTypeResponse.SMALL,
                            gender = GenderResponse.MALE,
                            imageUrl = "https://shop.peopet.co.kr/data/goods/388/2022/06/_temp_16557127733930view.jpg",
                        ),
                        PetDetailResponse(
                            name = "Buddy4",
                            description = "Buddy is a friendly dog who loves to play and meet new people.",
                            birthDate = LocalDate(2015, 5, 20),
                            sizeType = SizeTypeResponse.SMALL,
                            gender = GenderResponse.MALE,
                            imageUrl = "https://shop.peopet.co.kr/data/goods/388/2022/06/_temp_16557127733930view.jpg",
                        ),
                        PetDetailResponse(
                            name = "Buddy5",
                            description = "Buddy is a friendly dog who loves to play and meet new people.",
                            birthDate = LocalDate(2015, 5, 20),
                            sizeType = SizeTypeResponse.SMALL,
                            gender = GenderResponse.MALE,
                            imageUrl = "https://shop.peopet.co.kr/data/goods/388/2022/06/_temp_16557127733930view.jpg",
                        ),
                    ),
                isMine = true,
            ).toData()
        }
//        return runCatching { service.getFootprintInfo(footprintId).data.toData() }
    }
}
