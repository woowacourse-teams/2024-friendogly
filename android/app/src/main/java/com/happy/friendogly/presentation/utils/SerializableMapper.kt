package com.happy.friendogly.presentation.utils

import android.os.Build
import android.os.Bundle
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

inline fun <reified T> Bundle.bundleSerializable(
    key: String,
    serializer: KSerializer<T>
): T? {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            this.getString(key)?.let { Json.decodeFromString(serializer, it) }
        }

        else -> {
            this.getString(key)?.let { Json.decodeFromString(serializer, it) }
        }
    }
}

inline fun <reified T> Bundle.putSerializable(
    key: String,
    value: T,
    serializer: KSerializer<T>
) {
    val jsonString = Json.encodeToString(serializer, value)
    this.putString(key, jsonString)
}
