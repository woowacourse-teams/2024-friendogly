package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.mapper.toData
import com.woowacourse.friendogly.data.model.FootprintInfoDto
import com.woowacourse.friendogly.data.source.FootprintDataSource
import com.woowacourse.friendogly.remote.api.FootprintService

class FootprintDataSourceImpl(private val service: FootprintService) :
    FootprintDataSource {
    override suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfoDto> {
        return runCatching { service.getFootprintInfo(footprintId).data.toData() }
    }
}
