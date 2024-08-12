package com.happy.friendogly.presentation.ui.club.mapper

import com.happy.friendogly.domain.model.ClubFilterCondition
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ParticipationFilter

fun ParticipationFilter.toDomain(): ClubFilterCondition {
    return when (this) {
        ParticipationFilter.ENTIRE -> ClubFilterCondition.ALL
        ParticipationFilter.RECRUITMENT -> ClubFilterCondition.OPEN
        ParticipationFilter.POSSIBLE -> ClubFilterCondition.ABLE_TO_JOIN
    }
}

fun List<Gender>.toGenderGroupFilters(): List<ClubFilter> {
    return this.map { it.toClubFilter() }
}

fun List<SizeType>.toSizeGroupFilters(): List<ClubFilter> {
    return this.map { it.toClubFilter() }
}

fun Gender.toClubFilter(): ClubFilter.GenderFilter {
    return when (this) {
        Gender.MALE -> ClubFilter.GenderFilter.Male
        Gender.FEMALE -> ClubFilter.GenderFilter.Female
        Gender.MALE_NEUTERED -> ClubFilter.GenderFilter.NeutralizingMale
        Gender.FEMALE_NEUTERED -> ClubFilter.GenderFilter.NeutralizingFemale
    }
}

fun SizeType.toClubFilter(): ClubFilter.SizeFilter {
    return when (this) {
        SizeType.MEDIUM -> ClubFilter.SizeFilter.MediumDog
        SizeType.LARGE -> ClubFilter.SizeFilter.BigDog
        SizeType.SMALL -> ClubFilter.SizeFilter.SmallDog
    }
}

fun List<ClubFilter.SizeFilter>.toSizeTypes(): List<SizeType> {
    return this.mapNotNull {
        it.toSizeType()
    }
}

fun List<ClubFilter.GenderFilter>.toGenders(): List<Gender> {
    return this.mapNotNull {
        it.toGender()
    }
}

fun ClubFilter.GenderFilter.toGender(): Gender? {
    return when (this) {
        ClubFilter.GenderFilter.Female -> Gender.FEMALE
        ClubFilter.GenderFilter.Male -> Gender.MALE
        ClubFilter.GenderFilter.NeutralizingFemale -> Gender.FEMALE_NEUTERED
        ClubFilter.GenderFilter.NeutralizingMale -> Gender.MALE_NEUTERED
        ClubFilter.GenderFilter.Init -> null
    }
}

fun ClubFilter.SizeFilter.toSizeType(): SizeType? {
    return when (this) {
        ClubFilter.SizeFilter.SmallDog -> SizeType.SMALL
        ClubFilter.SizeFilter.MediumDog -> SizeType.MEDIUM
        ClubFilter.SizeFilter.BigDog -> SizeType.LARGE
        ClubFilter.SizeFilter.Init -> null
    }
}
