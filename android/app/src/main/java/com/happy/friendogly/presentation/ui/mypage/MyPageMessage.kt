package com.happy.friendogly.presentation.ui.mypage

sealed interface MyPageMessage {
    data object DefaultErrorMessage : MyPageMessage
}
