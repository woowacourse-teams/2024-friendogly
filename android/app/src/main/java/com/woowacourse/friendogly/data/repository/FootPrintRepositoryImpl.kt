package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.domain.model.FootPrintInfo
import com.woowacourse.friendogly.domain.repository.FootPrintRepository
import com.woowacourse.friendogly.remote.source.FootPrintDataSource

class FootPrintRepositoryImpl(private val dataSource: FootPrintDataSource) :
    FootPrintRepository {
    override suspend fun getFootPrintInfo(footprintId: Long): Result<FootPrintInfo> {
        return dataSource.getFootPrintInfo(footprintId)
            .mapCatching { dto -> dto.toDomain() }
    }
}
