package com.happy.friendogly.presentation.ui.registerpet

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import java.time.LocalDate

data class RegisterPetUiState(
    val isFirstTimeSetup: Boolean = true,
    val petBirthdayYear: Int = LocalDate.now().year,
    val petBirthdayMonth: Int = LocalDate.now().month.value,
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

fun SizeType.toPetSize(): PetSize {
    return when (this) {
        SizeType.SMALL -> PetSize.SMALL
        SizeType.MEDIUM -> PetSize.MEDIUM
        SizeType.LARGE -> PetSize.LARGE
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

fun Gender.toPetGender(): PetGender {
    return when (this) {
        Gender.MALE, Gender.MALE_NEUTERED -> PetGender.MAIL
        Gender.FEMALE, Gender.FEMALE_NEUTERED -> PetGender.FEMALE
    }
}
