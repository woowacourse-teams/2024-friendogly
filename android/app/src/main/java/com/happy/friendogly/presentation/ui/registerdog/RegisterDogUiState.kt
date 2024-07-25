package com.happy.friendogly.presentation.ui.registerdog

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
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
    ;

    fun toSizeType(): SizeType {
        return when (this) {
            SMALL -> SizeType.SMALL
            MEDIUM -> SizeType.MEDIUM
            LARGE -> SizeType.LARGE
        }
    }
}

enum class DogGender(val title: String) {
    MAIL("수컷"),
    FEMALE("암컷"),
    ;

    fun toGender(neutering: Boolean): Gender {
        return when (this) {
            MAIL -> {
                if (neutering) Gender.MALE_NEUTERED else Gender.MALE
            }

            FEMALE -> {
                if (neutering) Gender.FEMALE_NEUTERED else Gender.FEMALE
            }
        }
    }
}
