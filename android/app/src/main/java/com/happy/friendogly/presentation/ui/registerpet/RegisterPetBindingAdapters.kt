package com.happy.friendogly.presentation.ui.registerpet

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R

@SuppressLint("SetTextI18n")
@BindingAdapter("editTextDescriptionLength")
fun TextView.bindEditTextDescriptionLength(contents: String?) {
    val length = contents?.length ?: 0

    val color =
        if (length != 0) {
            ContextCompat.getColor(context, R.color.black)
        } else {
            ContextCompat.getColor(context, R.color.gray400)
        }

    this.apply {
        text = "$length/20"
        setTextColor(color)
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("dogEditBtnBackground")
fun TextView.bindDogEditBtnBackground(isProfileComplete: Boolean?) {
    val completed = isProfileComplete ?: return
    if (completed) {
        this.background = context.getDrawable(R.drawable.rect_coral400_fill_12)
        this.setTextColor(context.getColor(R.color.white))
    } else {
        this.background = context.getDrawable(R.drawable.rect_gray03_fill_16)
        this.setTextColor(context.getColor(R.color.gray700))
    }
}
