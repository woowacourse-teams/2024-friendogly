package com.happy.friendogly.domain.repository

import com.happy.friendogly.presentation.ui.woof.model.FootprintInfo

interface FootprintRepository {
    suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfo>
}
