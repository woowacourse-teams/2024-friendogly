package com.woowacourse.friendogly.presentation.ui.registerdog

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.woowacourse.friendogly.R

@SuppressLint("SetTextI18n")
@BindingAdapter("year", "month")
fun TextView.bindDogBirthday(
    year: Int?,
    month: Int?,
) {
    val yearStr = year.toString() + context.getString(R.string.birthday_year)
    val monthStr = context.getString(R.string.birthday_month).format(month)
    this.text = "$yearStr $monthStr"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("editTextDescriptionLength")
fun TextView.bindEditTextDescriptionLength(contents: String?) {
    val length = contents?.length ?: 0

    val color =
        if (length != 0) {
            ContextCompat.getColor(context, R.color.black)
        } else {
            ContextCompat.getColor(context, R.color.gray05)
        }

    this.apply {
        text = "$length/20"
        setTextColor(color)
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("dogName", "dogDescription", "profileImage")
fun TextView.bindDogEditBtnBackground(
    dogName: String?,
    dogDescription: String?,
    profileImage: Bitmap?,
) {
    if (dogName != "" && dogDescription != "" && profileImage != null) {
        this.background = context.getDrawable(R.drawable.rect_blue_fill_16)
        this.setTextColor(context.getColor(R.color.black))
    } else {
        this.background = context.getDrawable(R.drawable.rect_gray03_fill_16)
        this.setTextColor(context.getColor(R.color.gray08))
    }
}
