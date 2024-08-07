package com.happy.friendogly.presentation.ui.group.menu

import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.happy.friendogly.presentation.ui.group.detail.model.GroupDetailViewType

@BindingAdapter("myParticipation")
fun FrameLayout.bindMyParticipation(groupDetailViewType: GroupDetailViewType){
    this.isVisible = when(groupDetailViewType){
        GroupDetailViewType.RECRUITMENT,
        GroupDetailViewType.END_RECRUITMENT -> false
        GroupDetailViewType.PARTICIPATED,
        GroupDetailViewType.MINE -> true
    }
}

@BindingAdapter("myVisibleMenu")
fun FrameLayout.bindMyVisibleMenu(groupDetailViewType: GroupDetailViewType) {
    this.isVisible = groupDetailViewType == GroupDetailViewType.MINE
}

@BindingAdapter("userVisibleMenu")
fun FrameLayout.bindUserVisibleMenu(groupDetailViewType: GroupDetailViewType) {
    this.isVisible = groupDetailViewType != GroupDetailViewType.MINE
}
