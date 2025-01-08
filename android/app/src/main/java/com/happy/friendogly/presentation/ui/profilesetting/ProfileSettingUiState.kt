package com.happy.friendogly.presentation.ui.profilesetting

import android.graphics.Bitmap
import okhttp3.MultipartBody

data class ProfileSettingUiState(
    val isFirstTimeSetup: Boolean = true,
    val beforeName: String = "",
    val beforeProfileImageUrl: String? = null,
    val profileImage: Bitmap? = null,
    val profileImageUrl: String? = null,
    val profilePath: MultipartBody.Part? = null,
)
