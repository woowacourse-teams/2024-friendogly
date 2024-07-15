package com.woowacourse.friendogly.presentation.ui.profilesetting

import android.annotation.SuppressLint
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.woowacourse.friendogly.R

fun textChangeColor(
    text: TextView,
    color: Int,
    start: Int,
    end: Int,
): SpannableStringBuilder {
    val builder = SpannableStringBuilder(text.text.toString())

    builder.setSpan(
        ForegroundColorSpan(color),
        start,
        end,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
    )

    return builder
}

@SuppressLint("SetTextI18n")
@BindingAdapter("editTextLength")
fun TextView.bindEditTextLength(contents: String?) {
    val length = contents?.length ?: 0
    this.text = "$length/15"

    val color =
        if (length != 0) {
            ContextCompat.getColor(context, R.color.black)
        } else {
            ContextCompat.getColor(context, R.color.gray05)
        }

    this.text = textChangeColor(this, color, 0, length.toString().length)
    this.setTextColor(color)
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
