package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.FootprintInfoDto
import com.happy.friendogly.data.source.FootprintDataSource
import com.happy.friendogly.domain.model.WalkStatus
import com.happy.friendogly.remote.api.FootprintService
import com.happy.friendogly.remote.mapper.toData
import com.happy.friendogly.remote.model.response.FootprintInfoResponse
import com.happy.friendogly.remote.model.response.GenderResponse
import com.happy.friendogly.remote.model.response.SizeTypeResponse
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

class FootprintDataSourceImpl(private val service: FootprintService) :
    FootprintDataSource {
    override suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfoDto> {
        return runCatching {
            FootprintInfoResponse(
                memberName = "John Doe",
                petName = "Buddy",
                petDescription = "Buddy is a friendly dog who loves to play and meet new people.",
                petBirthDate = LocalDate(2015, 5, 20),
                petSizeType = SizeTypeResponse.SMALL,
                petGender = GenderResponse.MALE,
                footprintImageUrl = "https://shop.peopet.co.kr/data/goods/388/2022/06/_temp_16557127733930view.jpg",
                walkStatus = WalkStatus.AFTER,
                startWalkTime = LocalDateTime(2024, 7, 30, 21, 30, 0),
                endWalkTime = LocalDateTime(2024, 7, 30, 22, 0, 0),
                createdAt = LocalDateTime(2024, 7, 30, 21, 0, 0),
                isMine = true,
            ).toData()
        }
//        return runCatching { service.getFootprintInfo(footprintId).data.toData() }
    }
}
