package com.woowacourse.friendogly.data.source

import com.woowacourse.friendogly.data.model.FootPrintDto
import com.woowacourse.friendogly.remote.service.FootPrintService
import com.woowacourse.friendogly.remote.source.FootPrintDataSource

class FootPrintDataSourceImpl(private val footPrintService: FootPrintService) : FootPrintDataSource {
    override suspend fun getFootPrint(memberId: Long): Result<FootPrintDto> {
        return Result.success(FootPrintDto())
//        return footPrintService.getFootPrint(memberId)
    }
}
