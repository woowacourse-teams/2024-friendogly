package com.woowacourse.friendogly.presentation.ui.group.select

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.woowacourse.friendogly.R

@BindingAdapter("bindingSelectBorder")
fun View.bindDogSelect(
    dogSelectUiModel: DogSelectUiModel,
) {
    this.setBackgroundColor(
        if (dogSelectUiModel.isSelected) {
            ContextCompat.getColor(context,R.color.orange05)
        } else {
            ContextCompat.getColor(context,R.color.white)
        },
    )
}
