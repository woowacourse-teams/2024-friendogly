package com.happy.friendogly.presentation.ui.mypage

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType

@SuppressLint("SetTextI18n")
@BindingAdapter("name", "age", "isAtLeastOneYearOld")
fun TextView.binDogTitleAndAge(
    name: String?,
    age: Int?,
    isAtLeastOneYearOld: Boolean?,
) {
    isAtLeastOneYearOld ?: return
    text = name +
        if (isAtLeastOneYearOld) {
            context.getString(R.string.pet_age_format).format(age)
        } else {
            context.getString(R.string.pet_months_format).format(age)
        }
}

@BindingAdapter("genderTitle")
fun TextView.binGenderTitle(gender: Gender) {
    text =
        when (gender) {
            Gender.MALE -> context.getString(R.string.male)
            Gender.FEMALE -> context.getString(R.string.female)
            Gender.MALE_NEUTERED -> context.getString(R.string.male_enutered)
            Gender.FEMALE_NEUTERED -> context.getString(R.string.female_enutered)
        }
}

@BindingAdapter("petSizeTitle")
fun TextView.bindPetSizeTitle(sizeType: SizeType) {
    text =
        when (sizeType) {
            SizeType.SMALL -> context.getString(R.string.pet_small_size_title)
            SizeType.MEDIUM -> context.getString(R.string.pet_medium_size_title)
            SizeType.LARGE -> context.getString(R.string.pet_large_size_title)
        }
}
