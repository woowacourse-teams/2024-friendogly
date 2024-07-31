package com.happy.friendogly.presentation.ui.mypage

interface MyPageActionHandler {
    fun navigateToDogDetail(id: Long)

    fun navigateToRegisterDog(id: Long)

    fun navigateToProfileEdit()

    fun navigateToPetEdit(id: Long)
}
