package com.happy.friendogly.data.source

import com.happy.friendogly.data.model.FootprintInfoDto

interface FootprintDataSource {
    suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfoDto>
}
