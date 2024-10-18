package com.happy.friendogly.presentation.utils

import android.app.Activity
import android.content.res.Configuration
import androidx.fragment.app.Fragment

fun Activity.isSystemInDarkMode(): Boolean =
    this.resources.configuration.uiMode
        .and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

fun Fragment.isSystemInDarkMode(): Boolean =
    this.resources.configuration.uiMode
        .and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
