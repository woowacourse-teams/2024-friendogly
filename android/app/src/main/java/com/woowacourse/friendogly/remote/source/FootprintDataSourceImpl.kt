package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.model.FootprintInfoDto
import com.woowacourse.friendogly.data.source.FootprintDataSource
import com.woowacourse.friendogly.remote.api.FootprintService

class FootprintDataSourceImpl(private val service: FootprintService) :
    FootprintDataSource {
    override suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfoDto> {
//        return Result.success(
//            FootprintInfoDto(
//                memberName = "도도",
//                petName = "땡이",
//                petDescription = "안녕하세요! 땡이에요~",
//                petBirthDate = "2020-07-22",
//                petSizeType = "SMALL",
//                petGender = "FEMALE",
//                footprintImageUrl = "https://github.com/user-attachments/assets/9329234e-e47d-4fc5-b4b5-9f2a827b60b1",
//                isMine = false,
//                createdAt = "2024-07-22 11:24:11",
//            ),
//        )
        return runCatching { service.getFootprintInfo(footprintId) }
    }
}
