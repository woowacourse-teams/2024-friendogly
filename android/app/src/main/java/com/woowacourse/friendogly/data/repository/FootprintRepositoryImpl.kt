package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.domain.model.FootprintInfo
import com.woowacourse.friendogly.domain.repository.FootprintRepository
import com.woowacourse.friendogly.remote.source.FootprintDataSource

class FootprintRepositoryImpl(private val dataSource: FootprintDataSource) :
    FootprintRepository {
    override suspend fun getFootPrintInfo(footprintId: Long): Result<FootprintInfo> {
        return dataSource.getFootPrintInfo(footprintId)
            .mapCatching { dto -> dto.toDomain() }
    }
}
