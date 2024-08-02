package com.happy.friendogly.presentation.ui.registerpet

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.presentation.ui.registerpet.model.PetProfile
import okhttp3.MultipartBody
import java.time.LocalDate

data class RegisterPetUiState(
    val profilePath: MultipartBody.Part? = null,
    val petBirthdayYear: Int = LocalDate.now().year,
    val petBirthdayMonth: Int = LocalDate.now().month.value,
    val neutering: Boolean = false,
    val beforePetProfile: PetProfile? = null,
)

enum class PetSize(val title: String) {
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

enum class PetGender(val title: String) {
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
