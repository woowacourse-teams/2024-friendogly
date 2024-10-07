package com.happy.friendogly.presentation.ui.recentpet

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType

@BindingAdapter("roundedView")
fun View.bindRoundedView(recentPetViewType: RecentPetViewType?) {
    recentPetViewType ?: return
    val drawable =
        when (recentPetViewType.roundState) {
            RoundState.FIRST -> R.drawable.shape_gray100_fill_top_radius_16
            RoundState.MID -> R.drawable.shape_gray100_fill
            RoundState.LAST -> R.drawable.shape_gray100_fill_bottom_radius_16
            RoundState.FIRST_AND_LAST -> R.drawable.shape_gray100_fill_all_radius_16
        }

    setBackgroundResource(drawable)
}

@BindingAdapter("recentDogGender")
fun TextView.bindRecentDogGender(gender: Gender) {
    text =
        when (gender) {
            Gender.MALE -> context.getString(R.string.recent_dog_male)
            Gender.FEMALE -> context.getString(R.string.recent_dog_female)
            Gender.MALE_NEUTERED -> context.getString(R.string.recent_dog_male_neutered)
            Gender.FEMALE_NEUTERED -> context.getString(R.string.recent_dog_female_neutered)
        }
}

@BindingAdapter("recentDogSize")
fun TextView.bindRecentDogSize(sizeType: SizeType) {
    text =
        when (sizeType) {
            SizeType.SMALL -> context.getString(R.string.recent_dog_small)
            SizeType.MEDIUM -> context.getString(R.string.recent_dog_medium)
            SizeType.LARGE -> context.getString(R.string.recent_dog_large)
        }
}
