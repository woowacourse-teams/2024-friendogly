package com.woowacourse.friendogly.presentation.ui.group.list.model.groupfilter

sealed class GroupFilter(val filterName: String) {
    sealed class GenderFilter(filterName: String) : GroupFilter(filterName) {
        data object Female : GenderFilter(FEMALE_NAME)

        data object Male : GenderFilter(MALE_NAME)

        data object NeutralizingFemale : GenderFilter(NEUTRALIZING_FEMALE_NAME)

        data object NeutralizingMale : GenderFilter(NEUTRALIZING_MALE_NAME)
    }

    sealed class SizeFilter(filterName: String) : GroupFilter(filterName) {
        data object SmallDog : SizeFilter(SMALL_DOG_NAME)

        data object MediumDog : SizeFilter(MEDIUM_DOG_NAME)

        data object BigDog : SizeFilter(BIG_DOG_NAME)
    }

    companion object {
        private const val FEMALE_NAME = "암컷"
        private const val MALE_NAME = "수컷"
        private const val NEUTRALIZING_FEMALE_NAME = "중성화 암컷"
        private const val NEUTRALIZING_MALE_NAME = "중성화 수컷"
        private const val SMALL_DOG_NAME = "소형견"
        private const val MEDIUM_DOG_NAME = "중형견"
        private const val BIG_DOG_NAME = "대형견"
    }
}
