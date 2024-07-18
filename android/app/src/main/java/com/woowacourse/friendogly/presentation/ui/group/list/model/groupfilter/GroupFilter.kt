package com.woowacourse.friendogly.presentation.ui.group.list.model.groupfilter

sealed class GroupFilter(val filterName: String) {
    sealed class GenderFilter(filterName: String) : GroupFilter(filterName) {
        class Female : GenderFilter(FEMALE_NAME)

        class Male : GenderFilter(MALE_NAME)

        class NeutralizingFemale : GenderFilter(NEUTRALIZING_FEMALE_NAME)

        class NeutralizingMale : GenderFilter(NEUTRALIZING_MALE_NAME)
    }

    sealed class SizeFilter(filterName: String) : GroupFilter(filterName) {
        class SmallDog : SizeFilter(SMALL_DOG_NAME)

        class MediumDog : SizeFilter(MEDIUM_DOG_NAME)

        class BigDog : SizeFilter(BIG_DOG_NAME)
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
