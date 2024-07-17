package com.woowacourse.friendogly.presentation.ui.group.select

import android.graphics.Color
import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("bindingSelectBorder")
fun dogSelect(
    view: View,
    dogSelectUiModel: DogSelectUiModel,
) {
    view.setBackgroundColor(
        if (dogSelectUiModel.isSelected) {
            Color.GREEN
        } else {
            Color.GRAY
        }
    )
}
