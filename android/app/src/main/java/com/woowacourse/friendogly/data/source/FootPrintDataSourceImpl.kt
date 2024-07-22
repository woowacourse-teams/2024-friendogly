package com.woowacourse.friendogly.data.source

import com.woowacourse.friendogly.data.model.FootPrintInfoDto
import com.woowacourse.friendogly.remote.service.FootPrintService
import com.woowacourse.friendogly.remote.source.FootPrintDataSource

class FootPrintDataSourceImpl(private val footPrintService: FootPrintService) :
    FootPrintDataSource {
    override suspend fun getFootPrintInfo(footprintId: Long): Result<FootPrintInfoDto> {
        return Result.success(
            FootPrintInfoDto(
                memberName = "도도",
                petName = "땡이",
                petDescription = "안녕하세요! 땡이에요~",
                petBirthDate = "2020-07-22",
                petSizeType = "SMALL",
                petGender = "FEMALE",
                footprintImageUrl = "https://github.com/user-attachments/assets/9329234e-e47d-4fc5-b4b5-9f2a827b60b1",
                isMine = false,
            ),
        )
//        return footPrintService.getFootPrintInfo(footprintId)
    }
}
