package com.woowacourse.friendogly.presentation.ui.profilesetting

import android.graphics.Bitmap
import okhttp3.MultipartBody

data class ProfileSettingUiState(
    val profileImage: Bitmap? = null,
    val profilePath: MultipartBody.Part? = null,
)
