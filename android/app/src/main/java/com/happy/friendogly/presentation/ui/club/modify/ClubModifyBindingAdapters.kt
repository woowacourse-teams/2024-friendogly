package com.happy.friendogly.presentation.ui.club.modify

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R
import com.happy.friendogly.domain.model.ClubState

@BindingAdapter("selectModifyStateBackground")
fun View.bindSelectModifyStateBackground(clubState: ClubState) {
    val backgroundTint =
        if (clubState == ClubState.FULL) {
            ContextCompat.getColorStateList(
                context,
                R.color.gray300,
            )
        } else {
            ContextCompat.getColorStateList(
                context,
                R.color.coral300,
            )
        }
    this.backgroundTintList = backgroundTint
}

@BindingAdapter("selectModifyStateTypeStyle")
fun TextView.bindSelectModifyStateTypeStyle(clubState: ClubState) {
    val textStyle =
        if (clubState == ClubState.FULL) {
            R.style.Theme_AppCompat_TextView_SemiBold_Gray07_Size14
        } else {
            R.style.Theme_AppCompat_TextView_SemiBold_White_Size14
        }
    this.setTextAppearance(textStyle)
}

@BindingAdapter("selectModifyStateImageTint")
fun ImageView.bindSelectModifyStateImageTint(clubState: ClubState) {
    val backgroundTint =
        if (clubState == ClubState.FULL) {
            ContextCompat.getColorStateList(
                context,
                R.color.gray700,
            )
        } else {
            ContextCompat.getColorStateList(
                context,
                R.color.white,
            )
        }
    this.imageTintList = backgroundTint
}
