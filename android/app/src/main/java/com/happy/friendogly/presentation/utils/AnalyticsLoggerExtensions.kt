package com.happy.friendogly.presentation.utils

import com.happy.friendogly.analytics.AnalyticsHelper
import com.happy.friendogly.analytics.ParamKeys
import com.happy.friendogly.analytics.Types

fun AnalyticsHelper.logKakaoLoginClicked() {
    logEvent(
        type = Types.KAKAO_LOGIN_CLICKED,
        ParamKeys.LOGIN_ATTEMPT_TIME to System.currentTimeMillis(),
    )
}

fun AnalyticsHelper.logGoogleLoginClicked() {
    logEvent(
        type = Types.GOOGLE_LOGIN_CLICKED,
        ParamKeys.LOGIN_ATTEMPT_TIME to System.currentTimeMillis(),
    )
}
