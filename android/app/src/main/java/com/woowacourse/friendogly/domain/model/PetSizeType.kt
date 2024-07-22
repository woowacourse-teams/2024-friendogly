package com.woowacourse.friendogly.domain.model

enum class PetSizeType {
    SMALL,
    MEDIUM,
    LARGE, ;

    companion object {
        fun from(petSizeType: String): PetSizeType {
            return when (petSizeType) {
                "SMALL" -> SMALL
                "MEDIUM" -> MEDIUM
                "LARGE" -> LARGE
                else -> throw IllegalArgumentException("Unknown pet size type: $petSizeType")
            }
        }

        fun to(petSizeType: PetSizeType): String {
            return when (petSizeType) {
                SMALL -> "소형견"
                MEDIUM -> "중현견"
                LARGE -> "대형견"
            }
        }
    }
}
