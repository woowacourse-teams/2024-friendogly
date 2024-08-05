package com.happy.friendogly.domain.mapper

import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

fun List<Gender>.toGenderGroupFilters(): List<GroupFilter>{
    return this.map { it.toGroupFilter() }
}

fun List<SizeType>.toSizeGroupFilters(): List<GroupFilter>{
    return this.map { it.toGroupFilter() }
}

fun Gender.toGroupFilter() : GroupFilter{
    return when(this){
        Gender.MALE -> GroupFilter.GenderFilter.Male
        Gender.FEMALE -> GroupFilter.GenderFilter.Female
        Gender.MALE_NEUTERED -> GroupFilter.GenderFilter.NeutralizingMale
        Gender.FEMALE_NEUTERED -> GroupFilter.GenderFilter.NeutralizingFemale
    }
}

fun SizeType.toGroupFilter(): GroupFilter {
    return when(this){
        SizeType.MEDIUM -> GroupFilter.SizeFilter.MediumDog
        SizeType.LARGE -> GroupFilter.SizeFilter.BigDog
        SizeType.SMALL -> GroupFilter.SizeFilter.SmallDog
    }
}
