package com.happy.friendogly.presentation.utils

import android.content.Intent
import android.os.Build
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

inline fun <reified T> Intent.intentSerializable(
    key: String,
    serializer: KSerializer<T>
): T? {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
            this.getStringExtra(key)?.let { Json.decodeFromString(serializer, it) }
        }

        else -> {
            this.getStringExtra(key)?.let { Json.decodeFromString(serializer, it) }
        }
    }
}

inline fun <reified T> Intent.putSerializable(
    key: String,
    value: T,
    serializer: KSerializer<T>
) {
    val jsonString = Json.encodeToString(serializer, value)
    this.putExtra(key, jsonString)
}
