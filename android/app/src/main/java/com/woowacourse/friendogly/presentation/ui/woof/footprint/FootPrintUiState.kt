package com.woowacourse.friendogly.presentation.ui.woof.footprint

import com.woowacourse.friendogly.presentation.model.FootPrintInfoUiModel

sealed class FootPrintUiState {
    data object Loading : FootPrintUiState()

    data class Success(val footPrint: FootPrintInfoUiModel) : FootPrintUiState()

    data class Error(val exception: Throwable) : FootPrintUiState()
}
