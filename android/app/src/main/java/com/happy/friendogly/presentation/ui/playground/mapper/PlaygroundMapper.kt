package com.happy.friendogly.presentation.ui.playground.mapper

import com.happy.friendogly.presentation.ui.playground.model.MyPlayground
import com.happy.friendogly.presentation.ui.playground.model.Playground

fun Playground.toMyPlayground(): MyPlayground = MyPlayground(id = id, latitude = latitude, longitude = longitude)
