package com.woowacourse.friendogly.domain.model

enum class PetGender {
    MALE,
    FEMALE,
    MALE_NEUTERED,
    FEMALE_NEUTERED, ;

    companion object {
        fun from(petGender: String): PetGender {
            return when (petGender) {
                "MALE" -> MALE
                "FEMALE" -> FEMALE
                "MALE_NEUTERED" -> MALE_NEUTERED
                "FEMALE_NEUTERED" -> FEMALE_NEUTERED
                else -> throw IllegalArgumentException("Unknown pet gender: $petGender")
            }
        }

        fun to(petGender: PetGender): String {
            return when (petGender) {
                MALE -> "수컷"
                FEMALE -> "암컷"
                MALE_NEUTERED -> "중성화 수컷"
                FEMALE_NEUTERED -> "중성화 암컷"
            }
        }
    }
}
