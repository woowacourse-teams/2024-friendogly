package com.happy.friendogly.presentation.ui.woof.action

interface WoofActionHandler {
    fun clickMarkBtn()

    fun clickRegisterMarkerBtn()

    fun clickLocationBtn()

    fun clickMyFootprintBtn()

    fun clickStatusAll()

    fun clickStatusBefore()

    fun clickStatusOnGoing()

    fun clickStatusAfter()

    fun clickRefreshBtn()

    fun clickDeleteMyFootprintMarkerBtn()

    fun clickEndWalkBtn()

    fun clickBackBtn()

    fun clickCloseBtn()

    fun clickFootprintPetImage(petImageUrl: String)

    fun clickFootprintMemberName(memberId: Long)

    fun clickHelpBtn()
}
