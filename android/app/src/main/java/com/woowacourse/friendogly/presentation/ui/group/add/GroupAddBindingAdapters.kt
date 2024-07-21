package com.woowacourse.friendogly.presentation.ui.group.add

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.woowacourse.friendogly.presentation.ui.group.add.adapter.GroupAddAdapter
import com.woowacourse.friendogly.presentation.utils.toSoftwareBitmap

@BindingAdapter("currentPageProgress")
fun ProgressBar.bindProgress(currentPage: Int) {
    val progress = (currentPage + 1) * 100 / GroupAddAdapter.MAX_PAGE_SIZE
    this.progress = progress
}

@BindingAdapter("groupPoster")
fun ImageView.bindGroupPoster(bitmap: Bitmap?) {
    if (bitmap == null) return

    val softwareBitmap = bitmap.toSoftwareBitmap()

    Glide.with(context)
        .asBitmap()
        .load(softwareBitmap)
        .centerCrop()
        .into(this)
}
