package com.woowacourse.friendogly.presentation.ui.woof.footprint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.woowacourse.friendogly.domain.repository.FootPrintRepository

class FootPrintViewModelFactory(
    private val memberId: Long,
    private val footPrintRepository: FootPrintRepository,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FootPrintViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FootPrintViewModel(
                memberId = memberId,
                footPrintRepository = footPrintRepository,
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
