package com.woowacourse.friendogly.presentation.utils

import android.os.Build
import android.os.Bundle

inline fun <reified T> Bundle.bundleParcelable(
    key: String,
    clazz: Class<T>,
): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        this.getParcelable(key, clazz)
    } else {
        this.getParcelable(key)
    }
}
