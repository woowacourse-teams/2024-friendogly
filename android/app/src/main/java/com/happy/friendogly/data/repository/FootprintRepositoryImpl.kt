package com.happy.friendogly.data.repository

import com.happy.friendogly.data.mapper.toDomain
import com.happy.friendogly.data.source.FootprintDataSource
import com.happy.friendogly.domain.repository.FootprintRepository
import com.happy.friendogly.presentation.ui.woof.model.FootprintInfo

class FootprintRepositoryImpl(private val source: FootprintDataSource) :
    FootprintRepository {
    override suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfo> {
        return source.getFootprintInfo(footprintId)
            .mapCatching { dto -> dto.toDomain() }
    }
}
