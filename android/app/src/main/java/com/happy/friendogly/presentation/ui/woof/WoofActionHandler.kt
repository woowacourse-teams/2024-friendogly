package com.happy.friendogly.presentation.ui.woof

import com.naver.maps.map.overlay.Marker

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

    fun clickEndWalkBtn()

    fun clickBackBtn()

    fun clickCloseBtn()

    fun clickFootprintMarker(
        footprintId: Long,
        marker: Marker,
    )

    fun clickFootprintPetImage(petImageUrl: String)

    fun clickFootprintMemberName(memberId: Long)

    fun clickRegisterHelp()
}
