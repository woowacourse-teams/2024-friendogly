package com.happy.friendogly.presentation.ui.playground.action

sealed interface PlaygroundTrackingModeAction {
    data object NoFollowTrackingMode : PlaygroundTrackingModeAction

    data object FollowTrackingMode : PlaygroundTrackingModeAction

    data object FaceTrackingMode : PlaygroundTrackingModeAction
}
