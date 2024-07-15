package com.woowacourse.friendogly.presentation.ui.profilesetting

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.woowacourse.friendogly.R

@SuppressLint("SetTextI18n")
@BindingAdapter("editTextLength")
fun TextView.bindEditTextLength(contents: String?) {
    val length = contents?.length ?: 0

    val color =
        if (length != 0) {
            ContextCompat.getColor(context, R.color.black)
        } else {
            ContextCompat.getColor(context, R.color.gray05)
        }

    this.apply {
        text = "$length/15"
        setTextColor(color)
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("editBtnBackgroundTextColor")
fun TextView.bindEditBtnBackground(contents: String?) {
    val length = contents?.length ?: 0

    if (length > 0) {
        this.background = context.getDrawable(R.drawable.rect_blue_fill_16)
        this.setTextColor(context.getColor(R.color.black))
    } else {
        this.background = context.getDrawable(R.drawable.rect_gray03_fill_16)
        this.setTextColor(context.getColor(R.color.gray08))
    }
}
