package com.happy.friendogly.presentation.ui.group.select

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R

@BindingAdapter("bindingSelectBorder")
fun View.bindDogSelect(dogSelectUiModel: DogSelectUiModel) {
    this.setBackgroundColor(
        if (dogSelectUiModel.isSelected) {
            ContextCompat.getColor(context, R.color.coral300)
        } else {
            ContextCompat.getColor(context, R.color.white)
        },
    )
}
