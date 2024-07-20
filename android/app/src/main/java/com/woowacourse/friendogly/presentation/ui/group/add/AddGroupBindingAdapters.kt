package com.woowacourse.friendogly.presentation.ui.group.add

import android.util.Log
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.woowacourse.friendogly.presentation.ui.group.add.adapter.GroupAddAdapter

@BindingAdapter("currentPageProgress")
fun ProgressBar.bindProgress(currentPage: Int) {
    val progress = (currentPage + 1) * 100 / GroupAddAdapter.CURRENT_PAGE_SIZE
    this.progress = progress
}
