package com.happy.friendogly.presentation.ui.club.add

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.happy.friendogly.R
import com.happy.friendogly.presentation.ui.club.add.adapter.ClubAddAdapter.Companion.MAX_PAGE_SIZE
import com.happy.friendogly.presentation.utils.toSoftwareBitmap

@BindingAdapter("currentPageProgress")
fun ProgressBar.bindProgress(currentPage: Int) {
    val progress = (currentPage + 1) * 100 / (MAX_PAGE_SIZE)
    this.progress = progress
}

@BindingAdapter("clubPoster")
fun ImageView.bindClubPoster(bitmap: Bitmap?) {
    val softwareBitmap = bitmap?.toSoftwareBitmap()
    if (softwareBitmap == null){
        this.setImageResource(R.drawable.ic_club_normal)
    } else {
        Glide.with(context)
            .asBitmap()
            .load(softwareBitmap)
            .centerCrop()
            .into(this)
    }
}

@BindingAdapter("contentCount")
fun TextView.bindContentCount(content: String?) {
    this.text = context.getString(R.string.club_add_content_count, content?.length ?: 0)
}

@BindingAdapter("titleCount")
fun TextView.bindTitleCount(title: String?) {
    this.text = context.getString(R.string.club_add_title_count, title?.length ?: 0)
}

@BindingAdapter("validButtonBackground")
fun TextView.bindValidBackground(isValid: Boolean) {
    val backgroundTint =
        if (isValid) {
            ContextCompat.getColorStateList(
                context,
                R.color.coral400,
            )
        } else {
            ContextCompat.getColorStateList(context, R.color.gray300)
        }
    this.backgroundTintList = backgroundTint
}

@BindingAdapter("validButtonStyle")
fun TextView.bindValidStyle(isValid: Boolean) {
    val textStyle =
        if (isValid) {
            R.style.Theme_AppCompat_TextView_SemiBold_White_Size14
        } else {
            R.style.Theme_AppCompat_TextView_SemiBold_Gray07_Size14
        }
    this.setTextAppearance(textStyle)
}

@BindingAdapter("validNextButtonText")
fun TextView.bindValidNextButtonText(currentPage: Int) {
    this.text =
        if (currentPage == MAX_PAGE_SIZE - 1) {
            context.getString(R.string.club_list_commit_btn)
        } else {
            context.getString(R.string.club_list_next_btn)
        }
}
