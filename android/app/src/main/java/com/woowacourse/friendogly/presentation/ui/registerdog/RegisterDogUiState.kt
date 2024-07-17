package com.woowacourse.friendogly.presentation.ui.registerdog

import okhttp3.MultipartBody
import java.time.LocalDate

data class RegisterDogUiState(
    val profilePath: MultipartBody.Part? = null,
    val dogBirthdayYear: Int = LocalDate.now().year,
    val dogBirthdayMonth: Int = LocalDate.now().month.value,
    val neutering: Boolean = false,
)

enum class DogSize(val title: String) {
    SMALL("소형견"),
    MEDIUM("중형견"),
    LARGE("대형견"),
}

enum class DogGender(val title: String) {
    MAIL("수컷"),
    FEMALE("암컷"),
}
