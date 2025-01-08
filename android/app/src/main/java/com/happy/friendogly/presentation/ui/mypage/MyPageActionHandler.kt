package com.happy.friendogly.presentation.ui.mypage

interface MyPageActionHandler {
    fun navigateToPetDetail()

    fun navigateToRegisterDog(id: Long)

    fun navigateToProfileEdit()

    fun navigateToPetEdit()
}
