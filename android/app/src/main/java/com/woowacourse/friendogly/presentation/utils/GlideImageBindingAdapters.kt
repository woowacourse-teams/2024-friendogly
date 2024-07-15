package com.woowacourse.friendogly.presentation.utils

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.woowacourse.friendogly.R

@BindingAdapter("glide1000")
fun ImageView.bindGlide1000(uri: String?) {
    if (uri == null) return
    Glide.with(context)
        .load(uri)
        .transform(CenterCrop(), RoundedCorners(1000))
        .into(this)
}

@BindingAdapter("glideProfile1000")
fun ImageView.bindProfile1000(bitmap: Bitmap?) {
    if (bitmap == null) {
        this.setImageResource(R.drawable.img_dog)
        return
    }
    val softwareBitmap = bitmap.toSoftwareBitmap()

    Glide.with(context)
        .asBitmap()
        .load(softwareBitmap)
        .transform(CenterCrop(), RoundedCorners(1000))
        .into(this)
}
