package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.model.FootprintMarkBtnInfo
import com.woowacourse.friendogly.domain.repository.WoofRepository

class GetFootprintMarkBtnInfoUseCase(private val repository: WoofRepository) {
    suspend operator fun invoke(): Result<FootprintMarkBtnInfo> = repository.getFootprintMarkBtnInfo()
}
