package com.happy.friendogly.presentation.ui.petdetail

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.happy.friendogly.R
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType

@BindingAdapter("setUpIndicator")
fun LinearLayout.bindSetUpIndicator(size: Int) {
    removeAllViews()

    val layoutParams =
        LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        ).apply {
            leftMargin = 30
        }

    repeat(size) { index ->
        ImageView(context).apply {
            val drawableId =
                if (index == 0) R.drawable.shape_indicator_active else R.drawable.shape_indicator_inactive

            setImageDrawable(ContextCompat.getDrawable(context, drawableId))
            this.layoutParams = layoutParams
            addView(this)
        }
    }
}

@BindingAdapter("currentIndicator")
fun LinearLayout.bindCurrentIndicator(currentPage: Int) {
    if (childCount == 0) return
    val position = currentPage % childCount
    for (idx in 0 until childCount) {
        val imageView = getChildAt(idx) as ImageView
        val drawableId =
            if (idx == position) R.drawable.shape_indicator_active else R.drawable.shape_indicator_inactive
        imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableId))
    }
}

@BindingAdapter("genderDrawable")
fun ImageView.bindGenderDrawable(gender: Gender) {
    when (gender) {
        Gender.MALE -> this.setImageResource(R.drawable.img_dog_male)
        Gender.FEMALE -> this.setImageResource(R.drawable.img_dog_female)
        else -> {}
    }
}

@BindingAdapter("neuteredTitle")
fun TextView.bindNeuteredTitle(gender: Gender) {
    when (gender) {
        Gender.MALE_NEUTERED, Gender.FEMALE_NEUTERED -> {
            text = context.getString(R.string.neuterd_status)
            visibility = View.VISIBLE
        }

        else -> visibility = View.INVISIBLE
    }
}

@BindingAdapter("dogSize")
fun TextView.bindDogSize(sizeType: SizeType) {
    text =
        when (sizeType) {
            SizeType.SMALL -> context.getString(R.string.small_pet_description)
            SizeType.MEDIUM -> context.getString(R.string.medium_pet_description)
            SizeType.LARGE -> context.getString(R.string.large_pet_description)
        }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("age", "isAtLeastOneYearOld")
fun TextView.binDogTitleAndAge(
    age: Int?,
    isAtLeastOneYearOld: Boolean?,
) {
    isAtLeastOneYearOld ?: return
    text =
        if (isAtLeastOneYearOld) {
            context.getString(R.string.pet_age_format).format(age)
        } else {
            context.getString(R.string.pet_months_format).format(age)
        }
}

@BindingAdapter("viewPager2Swipe")
fun ViewPager2.bindViewPager2Swipe(petsDetail: PetsDetail) {
    isUserInputEnabled = petsDetail.data.size != 1
}
