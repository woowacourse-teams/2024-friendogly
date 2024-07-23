package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.model.FootprintInfo
import com.woowacourse.friendogly.domain.repository.FootprintRepository

class GetFootprintInfoUseCase(private val repository: FootprintRepository) {
    suspend operator fun invoke(footprintId: Long): Result<FootprintInfo> = repository.getFootprintInfo(footprintId)
}
