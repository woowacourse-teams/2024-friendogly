package com.happy.friendogly.presentation.ui.woof.action

interface WoofActionHandler {
    fun clickMarkBtn()

    fun clickRegisterMarkerBtn()

    fun clickLocationBtn()

    fun clickMyFootprintBtn()

    fun clickRefreshBtn()

    fun clickDeleteMyFootprintMarkerBtn()

    fun clickEndWalkBtn()

    fun clickBackBtn()

    fun clickCloseBtn()

    fun clickFootprintMemberName(memberId: Long)

    fun clickHelpBtn()
}
