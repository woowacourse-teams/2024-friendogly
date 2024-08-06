package com.happy.friendogly.presentation.ui.profilesetting

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R

@BindingAdapter("profileSettingTitle")
fun TextView.bindProfileSettingTitle(isFirstTimeSetup: Boolean) {
    text =
        if (isFirstTimeSetup) {
            context.getString(R.string.make_profile_title)
        } else {
            context.getString(R.string.make_profile_edit_title)
        }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("editTextLength")
fun TextView.bindEditTextLength(contents: String?) {
    val length = contents?.length ?: 0

    val color =
        if (length != 0) {
            ContextCompat.getColor(context, R.color.black)
        } else {
            ContextCompat.getColor(context, R.color.gray400)
        }

    this.apply {
        text = context.getString(R.string.user_name_length, length)
        setTextColor(color)
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("editBtnBackgroundTextColor")
fun TextView.bindEditBtnBackground(isButtonActive: Boolean) {
    if (isButtonActive) {
        this.background = context.getDrawable(R.drawable.rect_coral04_fill_16)
        this.setTextColor(context.getColor(R.color.gray100))
    } else {
        this.background = context.getDrawable(R.drawable.rect_gray01_fill_16)
        this.setTextColor(context.getColor(R.color.gray600))
    }
}
