package com.happy.friendogly.presentation.ui.woof.mapper

import com.happy.friendogly.presentation.ui.woof.model.PlayStatus
import com.happy.friendogly.presentation.ui.woof.model.PlaygroundArrival

fun PlaygroundArrival.toPresentation(): PlayStatus = if (isArrived) PlayStatus.PLAYING else PlayStatus.AWAY
