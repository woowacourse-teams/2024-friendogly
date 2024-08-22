package com.happy.friendogly.remote.model.request

import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import kotlinx.serialization.Serializable

@Serializable
data class FootprintRecentWalkStatusManualRequest(
    val walkStatus: WalkStatus,
)
