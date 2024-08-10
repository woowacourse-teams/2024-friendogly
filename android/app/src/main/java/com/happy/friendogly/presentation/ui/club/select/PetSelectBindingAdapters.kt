package com.happy.friendogly.presentation.ui.club.select

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R

@BindingAdapter("bindingSelectBorder")
fun View.bindDogSelect(petSelectUiModel: PetSelectUiModel) {
    this.setBackgroundColor(
        if (petSelectUiModel.isSelected) {
            ContextCompat.getColor(context, R.color.coral300)
        } else {
            ContextCompat.getColor(context, R.color.white)
        },
    )
}
