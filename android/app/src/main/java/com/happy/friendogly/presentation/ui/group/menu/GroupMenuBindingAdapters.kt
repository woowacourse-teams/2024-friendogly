package com.happy.friendogly.presentation.ui.group.menu

import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.happy.friendogly.domain.model.GroupDetailViewType

@BindingAdapter("myVisibleMenu")
fun FrameLayout.bindMyVisibleMenu(groupDetailViewType: GroupDetailViewType) {
    this.isVisible = groupDetailViewType == GroupDetailViewType.MINE
}

@BindingAdapter("userVisibleMenu")
fun FrameLayout.bindUserVisibleMenu(groupDetailViewType: GroupDetailViewType) {
    this.isVisible = groupDetailViewType != GroupDetailViewType.MINE
}
