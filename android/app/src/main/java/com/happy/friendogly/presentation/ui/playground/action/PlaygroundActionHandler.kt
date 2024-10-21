package com.happy.friendogly.presentation.ui.playground.action

interface PlaygroundActionHandler {
    fun clickPetExistenceBtn()

    fun clickRegisterMarkerBtn()

    fun clickLocationBtn()

    fun clickMyPlaygroundBtn()

    fun clickPlaygroundRefreshBtn()

    fun clickPlaygroundInfoRefreshBtn(playgroundId: Long)

    fun clickBackBtn()

    fun clickCloseBtn()

    fun clickPlaygroundPetDetail(memberId: Long)

    fun clickHelpBtn()

    fun clickPetImage(petImageUrl: String)

    fun clickPlaygroundMessage(message: String)

    fun clickJoinPlaygroundBtn(playgroundId: Long)

    fun clickLeavePlaygroundBtn()
}
