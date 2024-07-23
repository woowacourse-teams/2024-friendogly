package com.woowacourse.friendogly.presentation.ui.group.modify

import android.view.View
import android.widget.FrameLayout
import androidx.databinding.BindingAdapter
import com.woowacourse.friendogly.presentation.ui.group.detail.model.DetailViewType

@BindingAdapter("myVisibleMenu")
fun FrameLayout.bindMyVisibleMenu(detailViewType: DetailViewType) {
    this.visibility =
        if (detailViewType == DetailViewType.MINE) {
            View.VISIBLE
        } else {
            View.GONE
        }
}

@BindingAdapter("userVisibleMenu")
fun FrameLayout.bindUserVisibleMenu(detailViewType: DetailViewType) {
    this.visibility =
        if (detailViewType == DetailViewType.MINE) {
            View.GONE
        } else {
            View.VISIBLE
        }
}
