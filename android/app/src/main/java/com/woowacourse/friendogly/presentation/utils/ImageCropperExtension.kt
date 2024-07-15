package com.woowacourse.friendogly.presentation.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.woowacourse.friendogly.R

fun AppCompatActivity.launchImageCrop(
    uri: Uri,
    onSuccess: (bitmap: Bitmap) -> Unit,
) {
    val imageCropperOptions =
        CropImageOptions(
            fixAspectRatio = true,
            aspectRatioX = 1,
            aspectRatioY = 1,
            toolbarColor = Color.WHITE,
            toolbarBackButtonColor = Color.BLACK,
            toolbarTintColor = Color.BLACK,
            allowFlipping = false,
            allowRotation = false,
            cropMenuCropButtonTitle = getString(R.string.image_cropper_done),
            imageSourceIncludeCamera = false,
        )

    val imageCropLauncher =
        registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                val uriContent = result.uriContent ?: return@registerForActivityResult
                val bitmap =
                    if (Build.VERSION.SDK_INT < 28) {
                        MediaStore.Images.Media.getBitmap(
                            contentResolver,
                            uriContent,
                        )
                    } else {
                        val source =
                            ImageDecoder.createSource(contentResolver, uriContent)
                        ImageDecoder.decodeBitmap(source)
                    }
                onSuccess(bitmap)
            }
        }

    val cropOptions = CropImageContractOptions(uri, imageCropperOptions)
    imageCropLauncher.launch(cropOptions)
}
