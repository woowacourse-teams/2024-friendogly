package com.happy.friendogly.domain.mapper

import com.happy.friendogly.domain.model.ClubFilterCondition
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.happy.friendogly.presentation.ui.group.model.groupfilter.ParticipationFilter


fun ParticipationFilter.toDomain(): ClubFilterCondition {
    return when (this) {
        ParticipationFilter.ENTIRE -> ClubFilterCondition.ALL
        ParticipationFilter.RECRUITMENT -> ClubFilterCondition.OPEN
        ParticipationFilter.POSSIBLE -> ClubFilterCondition.ABLE_TO_JOIN
    }
}

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

fun List<GroupFilter.SizeFilter>.toSizeTypes(): List<SizeType> {
    return this.mapNotNull {
        it.toSizeType()
    }
}

fun List<GroupFilter.GenderFilter>.toGenders(): List<Gender> {
    return this.mapNotNull {
        it.toGender()
    }
}

fun GroupFilter.GenderFilter.toGender(): Gender? {
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
