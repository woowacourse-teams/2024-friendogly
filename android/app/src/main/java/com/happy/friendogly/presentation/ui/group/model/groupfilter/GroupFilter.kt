package com.happy.friendogly.presentation.ui.group.model.groupfilter

sealed class GroupFilter(val filterName: String) {
    sealed class GenderFilter(filterName: String) : GroupFilter(filterName) {
        data object Init : GenderFilter(GENDER_FILTER_SUBJECT)

        data object Female : GenderFilter(FEMALE_NAME)

        data object Male : GenderFilter(MALE_NAME)

        data object NeutralizingFemale : GenderFilter(NEUTRALIZING_FEMALE_NAME)

        data object NeutralizingMale : GenderFilter(NEUTRALIZING_MALE_NAME)
    }

    sealed class SizeFilter(filterName: String) : GroupFilter(filterName) {
        data object Init : SizeFilter(SIZE_FILTER_SUBJECT)

        data object SmallDog : SizeFilter(SMALL_DOG_NAME)

        data object MediumDog : SizeFilter(MEDIUM_DOG_NAME)

        data object BigDog : SizeFilter(BIG_DOG_NAME)
    }

    companion object {
        fun findGroupFilter(filterName: String): GroupFilter? {
            return when (filterName) {
                FEMALE_NAME -> GenderFilter.Female
                MALE_NAME -> GenderFilter.Male
                NEUTRALIZING_FEMALE_NAME -> GenderFilter.NeutralizingFemale
                NEUTRALIZING_MALE_NAME -> GenderFilter.NeutralizingMale
                SMALL_DOG_NAME -> SizeFilter.SmallDog
                MEDIUM_DOG_NAME -> SizeFilter.MediumDog
                BIG_DOG_NAME -> SizeFilter.BigDog
                else -> null
            }
        }

        fun makeGroupFilterEntry(): List<GroupFilter> {
            return listOf(
                SizeFilter.SmallDog,
                SizeFilter.MediumDog,
                SizeFilter.BigDog,
                GenderFilter.Male,
                GenderFilter.Female,
                GenderFilter.NeutralizingMale,
                GenderFilter.NeutralizingFemale,
            )
        }

        fun makeSizeFilterEntry(): List<GroupFilter.SizeFilter> {
            return listOf(
                SizeFilter.SmallDog,
                SizeFilter.MediumDog,
                SizeFilter.BigDog,
            )
        }

        fun makeGenderFilterEntry(): List<GroupFilter.GenderFilter> {
            return listOf(
                GenderFilter.Male,
                GenderFilter.Female,
                GenderFilter.NeutralizingMale,
                GenderFilter.NeutralizingFemale,
            )
        }

        const val FEMALE_NAME = "암컷"
        const val MALE_NAME = "수컷"
        const val NEUTRALIZING_FEMALE_NAME = "중성화 암컷"
        const val NEUTRALIZING_MALE_NAME = "중성화 수컷"
        const val SMALL_DOG_NAME = "소형견"
        const val MEDIUM_DOG_NAME = "중형견"
        const val BIG_DOG_NAME = "대형견"

        const val GENDER_FILTER_SUBJECT = "성별"
        const val SIZE_FILTER_SUBJECT = "사이즈"
    }
}
