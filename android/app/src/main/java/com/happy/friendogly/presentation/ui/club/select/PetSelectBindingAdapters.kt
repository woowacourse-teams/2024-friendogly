package com.happy.friendogly.presentation.ui.club.select

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R

@BindingAdapter("bindingSelectBorder")
fun View.bindDogSelect(petSelectUiModel: PetSelectUiModel) {
    this.setBackgroundResource(
        if (petSelectUiModel.isSelected) {
            R.drawable.rect_coral04_fill_16
        } else {
            R.drawable.rect_white_fill_radius16
        },
    )
}

@BindingAdapter("bindSelectPrevent")
fun View.bindSelectPrevent(selectable: Boolean) {
    visibility =
        if (selectable) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
}

@BindingAdapter("bindSelectPreventContent")
fun TextView.bindSelectPreventContent(petUiModel: PetSelectUiModel) {
    text =
        if (petUiModel.selectable) {
            context.getString(R.string.dog_select_can_select).format(petUiModel.name)
        } else {
            context.getString(R.string.dog_select_prevent_select).format(petUiModel.name)
        }
}
