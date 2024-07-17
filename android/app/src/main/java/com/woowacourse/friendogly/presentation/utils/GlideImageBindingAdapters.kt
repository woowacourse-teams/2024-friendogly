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

@BindingAdapter("glideProfile1000")
fun ImageView.bindProfile1000(profilePath: String?) {
    if (profilePath == null) {
        this.setImageResource(R.drawable.img_dog)
        return
    }

    Glide.with(context)
        .asBitmap()
        .load(profilePath)
        .transform(CenterCrop(), RoundedCorners(1000))
        .into(this)
}

@BindingAdapter("dogProfile")
fun ImageView.bindDogProfile(bitmap: Bitmap?) {
    if (bitmap == null) return

    val softwareBitmap = bitmap.toSoftwareBitmap()

    Glide.with(context)
        .asBitmap()
        .load(softwareBitmap)
        .centerCrop()
        .into(this)
}

@BindingAdapter("urlToImage")
fun ImageView.bindUrlToImage(imageUrl: String?) {
    imageUrl?.let { url ->
        Glide.with(context)
            .load(url)
            .centerCrop()
            .into(this)
    }
}
