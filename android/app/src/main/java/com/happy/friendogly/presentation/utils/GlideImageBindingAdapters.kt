package com.happy.friendogly.presentation.utils

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.happy.friendogly.R

@BindingAdapter("glide")
fun ImageView.bindGlide(uri: String?) {
    if (uri.isNullOrBlank()) {
        this.setImageResource(R.drawable.img_profile_normal)
        return
    }
    Glide.with(context)
        .load(uri)
        .into(this)
}

@BindingAdapter("glideProfile1000")
fun ImageView.bindProfile1000(bitmap: Bitmap?) {
    if (bitmap == null) {
        this.setImageResource(R.drawable.img_profile_normal)
        return
    }
    val softwareBitmap = bitmap.toSoftwareBitmap()

    Glide.with(context)
        .asBitmap()
        .load(softwareBitmap)
        .transform(CenterCrop(), RoundedCorners(1000))
        .into(this)
}

@BindingAdapter("imageBitmap", "imageUrl")
fun ImageView.bindGlideProfileImage(
    imageBitmap: Bitmap?,
    imageUrl: String?,
) {
    if (imageBitmap == null && imageUrl.isNullOrBlank()) {
        this.setImageResource(R.drawable.img_profile_normal)
        return
    }
    if (imageBitmap != null) {
        val softwareBitmap = imageBitmap.toSoftwareBitmap()

        Glide.with(context)
            .asBitmap()
            .load(softwareBitmap)
            .transform(CenterCrop(), RoundedCorners(1000))
            .into(this)
    } else {
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .transform(CenterCrop(), RoundedCorners(1000))
            .into(this)
    }
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

@BindingAdapter("petImageBitmap", "petImageUrl")
fun ImageView.bindGlidePetProfileImage(
    imageBitmap: Bitmap?,
    imageUrl: String?,
) {
    if (imageBitmap == null && imageUrl.isNullOrBlank()) {
        return
    }
    if (imageBitmap != null) {
        val softwareBitmap = imageBitmap.toSoftwareBitmap()

        Glide.with(context)
            .asBitmap()
            .load(softwareBitmap)
            .centerCrop()
            .into(this)
    } else {
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .centerCrop()
            .into(this)
    }
}

@BindingAdapter("urlToImage")
fun ImageView.bindUrlToImage(imageUrl: String?) {
    imageUrl?.let { url ->
        Glide.with(context)
            .load(url)
            .into(this)
    }
}
