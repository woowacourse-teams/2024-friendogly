package com.happy.friendogly.domain.mapper

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

fun List<Gender>.toGenderGroupFilters(): List<GroupFilter> {
    return this.map { it.toGroupFilter() }
}

fun List<SizeType>.toSizeGroupFilters(): List<GroupFilter> {
    return this.map { it.toGroupFilter() }
}

fun Gender.toGroupFilter(): GroupFilter.GenderFilter {
    return when (this) {
        Gender.MALE -> GroupFilter.GenderFilter.Male
        Gender.FEMALE -> GroupFilter.GenderFilter.Female
        Gender.MALE_NEUTERED -> GroupFilter.GenderFilter.NeutralizingMale
        Gender.FEMALE_NEUTERED -> GroupFilter.GenderFilter.NeutralizingFemale
    }
}

fun SizeType.toGroupFilter(): GroupFilter.SizeFilter {
    return when (this) {
        SizeType.MEDIUM -> GroupFilter.SizeFilter.MediumDog
        SizeType.LARGE -> GroupFilter.SizeFilter.BigDog
        SizeType.SMALL -> GroupFilter.SizeFilter.SmallDog
    }
}

fun List<GroupFilter.SizeFilter>.toSizeType(): List<SizeType> {
    return this.mapNotNull {
        it.toSizeType()
    }
}

fun List<GroupFilter.GenderFilter>.toGender(): List<Gender> {
    return this.mapNotNull {
        it.toSizeType()
    }
}

fun GroupFilter.GenderFilter.toSizeType(): Gender? {
    return when (this) {
        GroupFilter.GenderFilter.Female -> Gender.FEMALE
        GroupFilter.GenderFilter.Male -> Gender.MALE
        GroupFilter.GenderFilter.NeutralizingFemale -> Gender.FEMALE_NEUTERED
        GroupFilter.GenderFilter.NeutralizingMale -> Gender.MALE_NEUTERED
        GroupFilter.GenderFilter.Init -> null
    }
}

fun GroupFilter.SizeFilter.toSizeType(): SizeType? {
    return when (this) {
        GroupFilter.SizeFilter.SmallDog -> SizeType.SMALL
        GroupFilter.SizeFilter.MediumDog -> SizeType.MEDIUM
        GroupFilter.SizeFilter.BigDog -> SizeType.LARGE
        GroupFilter.SizeFilter.Init -> null
    }
}
