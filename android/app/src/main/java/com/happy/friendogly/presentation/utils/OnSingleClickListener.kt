package com.happy.friendogly.presentation.utils

import android.view.View
import androidx.databinding.BindingAdapter

class OnSingleClickListener(
    private val clickListener: View.OnClickListener,
    private val intervalMs: Long = 1000,
) : View.OnClickListener {
    private var lastClickTime = 0L

    override fun onClick(v: View?) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= intervalMs) {
            lastClickTime = currentTime
            if (v == null) return
            clickListener.onClick(v)
        }
    }
}

@BindingAdapter("onSingleClick")
fun View.setOnSingleClickListener(clickListener: View.OnClickListener?) {
    clickListener?.also {
        setOnClickListener(OnSingleClickListener(it))
    } ?: setOnClickListener(null)
}
