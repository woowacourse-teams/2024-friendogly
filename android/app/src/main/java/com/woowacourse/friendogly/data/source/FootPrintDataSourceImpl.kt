package com.woowacourse.friendogly.data.source

import com.woowacourse.friendogly.data.model.FootPrintInfoDto
import com.woowacourse.friendogly.remote.service.FootPrintService
import com.woowacourse.friendogly.remote.source.FootPrintDataSource

class FootPrintDataSourceImpl(private val footPrintService: FootPrintService) : FootPrintDataSource {
    override suspend fun getFootPrintInfo(memberId: Long): Result<FootPrintInfoDto> {
        return Result.success(FootPrintInfoDto())
//        return footPrintService.getFootPrintInfo(memberId)
    }
}
