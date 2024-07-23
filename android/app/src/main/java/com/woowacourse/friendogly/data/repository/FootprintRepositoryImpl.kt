package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.data.source.FootprintDataSource
import com.woowacourse.friendogly.domain.model.FootprintInfo
import com.woowacourse.friendogly.domain.repository.FootprintRepository

class FootprintRepositoryImpl(private val source: FootprintDataSource) :
    FootprintRepository {
    override suspend fun getFootprintInfo(footprintId: Long): Result<FootprintInfo> {
        return source.getFootprintInfo(footprintId)
            .mapCatching { dto -> dto.toDomain() }
    }
}
