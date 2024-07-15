package com.woowacourse.friendogly.presentation.ui.registerdog

import android.graphics.Bitmap
import okhttp3.MultipartBody

data class RegisterDogUiState(
    val profileImage: Bitmap? = null,
    val profilePath: MultipartBody.Part? = null,
)
