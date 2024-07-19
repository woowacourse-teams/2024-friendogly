package com.woowacourse.friendogly.presentation.ui.woof

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.woowacourse.friendogly.R

@BindingAdapter("footPrintWalking")
fun TextView.bindFootPrintWalking(dogName: String) {
    val spannableString =
        SpannableString(String.format(resources.getString(R.string.woof_dog_walking), dogName))
    spannableString.setSpan(
        ForegroundColorSpan(resources.getColor(R.color.orange07)),
        0,
        dogName.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
    )

    text = spannableString
}
