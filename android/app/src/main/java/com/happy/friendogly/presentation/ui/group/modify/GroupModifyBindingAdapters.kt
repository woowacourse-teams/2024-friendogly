package com.happy.friendogly.presentation.ui.group.modify

import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.happy.friendogly.presentation.ui.group.detail.model.DetailViewType

@BindingAdapter("myVisibleMenu")
fun FrameLayout.bindMyVisibleMenu(detailViewType: DetailViewType) {
    this.isVisible = detailViewType == DetailViewType.MINE
}

@BindingAdapter("userVisibleMenu")
fun FrameLayout.bindUserVisibleMenu(detailViewType: DetailViewType) {
    this.isVisible = detailViewType != DetailViewType.MINE
}
