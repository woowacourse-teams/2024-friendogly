package com.happy.friendogly.presentation.ui.playground.mapper

import com.happy.friendogly.presentation.ui.playground.model.PlayStatus
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundArrival

fun PlaygroundArrival.toPresentation(): PlayStatus = if (isArrived) PlayStatus.PLAYING else PlayStatus.AWAY
