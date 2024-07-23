package com.woowacourse.friendogly.presentation.ui.woof

sealed interface WoofSnackbarActions {
    data object ShowSettingSnackbar : WoofSnackbarActions
}
