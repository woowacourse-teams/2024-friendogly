package com.woowacourse.friendogly.presentation.ui.woof.footprint

sealed class FootPrintUiState {
    data object Loading : FootPrintUiState()

    data class Success(val footPrint: FootPrintUiModel) : FootPrintUiState()

    data class Error(val exception: Throwable) : FootPrintUiState()
}
