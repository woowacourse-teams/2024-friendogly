package com.happy.friendogly.domain.repository

import com.happy.friendogly.domain.model.FootprintInfo

interface FootprintRepository {
    suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfo>
}
