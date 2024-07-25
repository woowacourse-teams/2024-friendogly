package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.model.LandMark
import com.woowacourse.friendogly.domain.repository.WoofRepository

class GetLandMarksUseCase(private val repository: WoofRepository) {
    suspend operator fun invoke(): Result<List<LandMark>> = repository.getLandMarks()
}
