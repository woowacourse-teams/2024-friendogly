package com.woowacourse.friendogly.presentation.ui.woof

sealed interface WoofSnackbarActions {
    data object ShowSettingSnackbar : WoofSnackbarActions

    data class ShowCantMarkSnackbar(
        val remainingTime: Int,
    ) : WoofSnackbarActions
}
