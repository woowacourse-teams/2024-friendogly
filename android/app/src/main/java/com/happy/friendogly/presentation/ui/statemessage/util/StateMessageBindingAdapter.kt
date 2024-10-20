package com.happy.friendogly.presentation.ui.statemessage.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R

@BindingAdapter("messageLength")
fun TextView.bindMessageLength(message: String) {
    text = resources.getString(R.string.message_length, message.length)
}

@BindingAdapter("clearBtnVisibility")
fun ImageView.bindMessageLength(message: String) {
    isVisible = message.isNotEmpty()
}
