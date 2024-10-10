package com.happy.friendogly.presentation.ui.woof.action

interface WoofActionHandler {
    fun clickPetExistenceBtn()

    fun clickRegisterMarkerBtn()

    fun clickLocationBtn()

    fun clickMyPlaygroundBtn()

    fun clickRefreshBtn()

    fun clickBackBtn()

    fun clickCloseBtn()

    fun clickPlaygroundMemberName(memberId: Long)

    fun clickHelpBtn()

    fun clickPetImage(petImageUrl: String)

    fun clickJoinPlaygroundBtn(playgroundId: Long)

    fun clickExitPlaygroundBtn()
}
