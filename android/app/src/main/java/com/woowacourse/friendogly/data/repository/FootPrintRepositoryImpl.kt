package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.data.mapper.toDomain
import com.woowacourse.friendogly.domain.model.FootPrintInfo
import com.woowacourse.friendogly.domain.repository.FootPrintRepository
import com.woowacourse.friendogly.remote.source.FootPrintDataSource

class FootPrintRepositoryImpl(private val remoteFootPrintDataSource: FootPrintDataSource) :
    FootPrintRepository {
    override suspend fun getFootPrintInfo(memberId: Long): Result<FootPrintInfo> {
        return remoteFootPrintDataSource.getFootPrintInfo(memberId)
            .mapCatching { footPrintDto -> footPrintDto.toDomain() }
    }
}
