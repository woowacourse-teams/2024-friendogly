package com.woowacourse.friendogly.data.repository

import com.woowacourse.friendogly.data.mapper.toDomainModel
import com.woowacourse.friendogly.domain.repository.FootPrintRepository
import com.woowacourse.friendogly.presentation.ui.woof.footprint.FootPrint
import com.woowacourse.friendogly.remote.source.FootPrintDataSource

class FootPrintRepositoryImpl(private val remoteFootPrintDataSource: FootPrintDataSource) :
    FootPrintRepository {
    override suspend fun getFootPrint(memberId: Long): Result<FootPrint> {
        return remoteFootPrintDataSource.getFootPrint(memberId).mapCatching { it.toDomainModel() }
    }
}
