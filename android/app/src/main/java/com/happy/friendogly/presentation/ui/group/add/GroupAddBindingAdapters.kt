package com.happy.friendogly.presentation.ui.group.add

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.happy.friendogly.R
import com.happy.friendogly.presentation.ui.group.add.adapter.GroupAddAdapter
import com.happy.friendogly.presentation.ui.group.detail.bindDetailViewTypeBackground
import com.happy.friendogly.presentation.ui.group.detail.model.GroupDetailViewType
import com.happy.friendogly.presentation.utils.toSoftwareBitmap

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

@BindingAdapter("contentCount")
fun TextView.bindContentCount(content: String?) {
    this.text = context.getString(R.string.group_add_content_count, content?.length ?: 0)
}

@BindingAdapter("titleCount")
fun TextView.bindTitleCount(title: String?) {
    this.text = context.getString(R.string.group_add_title_count, title?.length ?: 0)
}

@BindingAdapter("validButtonBackground")
fun TextView.bindValidBackground(isValid: Boolean){
    val backgroundTint =
        when (isValid) {
            true ->
                ContextCompat.getColorStateList(
                    context,
                    R.color.coral400,
                )

            false -> ContextCompat.getColorStateList(context, R.color.gray400)
        }
    this.backgroundTintList = backgroundTint
}

@BindingAdapter("validButtonStyle")
fun TextView.bindValidStyle(isValid: Boolean) {
    val textStyle =
        when (isValid) {
            true -> R.style.Theme_AppCompat_TextView_SemiBold_White_Size16
            false -> R.style.Theme_AppCompat_TextView_SemiBold_Black_Size16
        }
    this.setTextAppearance(textStyle)
}
