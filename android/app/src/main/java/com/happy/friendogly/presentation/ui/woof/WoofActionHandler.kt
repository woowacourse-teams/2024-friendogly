package com.happy.friendogly.presentation.ui.woof

import com.naver.maps.map.overlay.Marker

interface WoofActionHandler {
    fun clickMarkBtn()

    fun clickRegisterMarkerBtn()

    fun clickLocationBtn()

    fun clickMyFootprintBtn()

    fun clickRefreshBtn()

    fun clickEndWalkBtn()

    fun clickBackBtn()

    fun clickCloseBtn()

    fun clickFootprint(
        footprintId: Long,
        marker: Marker,
    )

    fun clickFootprintPetImage(petImageUrl: String)

    fun clickFootprintMemberName(memberId: Long)

    fun clickRegisterHelp()
}
