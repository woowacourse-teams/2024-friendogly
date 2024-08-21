package com.happy.friendogly.presentation.ui.mypage

sealed interface MyPageMessage {
    data object DefaultErrorMessage : MyPageMessage

    data object ServerErrorMessage : MyPageMessage

    data object NoInternetMessage : MyPageMessage
}
