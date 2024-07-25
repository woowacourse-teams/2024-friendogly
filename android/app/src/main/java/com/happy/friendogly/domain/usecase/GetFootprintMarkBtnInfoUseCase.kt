package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.FootprintMarkBtnInfo
import com.happy.friendogly.domain.repository.WoofRepository

class GetFootprintMarkBtnInfoUseCase(private val repository: WoofRepository) {
    suspend operator fun invoke(): Result<FootprintMarkBtnInfo> = repository.getFootprintMarkBtnInfo()
}
