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
        text = context.getString(R.string.pet_name_length, length)
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

@BindingAdapter("registerPetTitle")
fun TextView.bindRegisterPetTitle(isFirstTimeSetup: Boolean) {
    text =
        if (isFirstTimeSetup) {
            context.getString(R.string.register_pet_toolbar_title)
        } else {
            context.getString(R.string.patch_pet_toolbar_title)
        }
}
