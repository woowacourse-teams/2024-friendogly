package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.repository.WoofRepository
import com.happy.friendogly.presentation.ui.woof.model.FootprintMarkBtnInfo
import javax.inject.Inject

class GetFootprintMarkBtnInfoUseCase @Inject constructor(private val repository: WoofRepository) {
    suspend operator fun invoke(): Result<FootprintMarkBtnInfo> = repository.getFootprintMarkBtnInfo()
}
