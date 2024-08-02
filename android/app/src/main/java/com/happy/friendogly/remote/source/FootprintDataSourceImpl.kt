package com.happy.friendogly.remote.source

import com.happy.friendogly.data.model.FootprintInfoDto
import com.happy.friendogly.data.source.FootprintDataSource
import com.happy.friendogly.remote.api.FootprintService
import com.happy.friendogly.remote.mapper.toData

class FootprintDataSourceImpl(private val service: FootprintService) :
    FootprintDataSource {
    override suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfoDto> {
        return runCatching { service.getFootprintInfo(footprintId).data.toData() }
    }
}
