package com.happy.friendogly.firebase.analytics

import android.content.Context
import android.os.Bundle
import androidx.core.os.bundleOf
import com.google.firebase.analytics.FirebaseAnalytics

class AnalyticsHelper(context: Context) {
    private val analytics = FirebaseAnalytics.getInstance(context)

    fun logEvent(
        type: String,
        vararg params: Pair<String, Any?>,
    ) {
        val bundle =
            bundleOf(
                *params.map { (key, value) ->
                    key.take(40) to value.toString().take(100)
                }.toTypedArray(),
            )
        logEvent(type, bundle)
    }

    fun logEvent(
        type: String,
        params: Bundle? = null,
    ) {
        analytics.logEvent(type, params)
    }
}
