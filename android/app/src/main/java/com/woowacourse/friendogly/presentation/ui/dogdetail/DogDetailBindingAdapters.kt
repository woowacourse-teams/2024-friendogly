package com.woowacourse.friendogly.presentation.ui.dogdetail

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.woowacourse.friendogly.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
fun ImageView.bindGenderDrawable(gender: String) {
    if (gender == "수컷") {
        this.setImageResource(R.drawable.img_dog_male)
    } else {
        this.setImageResource(R.drawable.img_dog_female)
    }
}

@BindingAdapter("neuteredTitle")
fun TextView.bindNeuteredTitle(isNeutered: Boolean) {
    if (isNeutered) {
        this.apply {
            text = "중성화를 했어요"
            visibility = View.VISIBLE
        }
    } else {
        this.visibility = View.GONE
    }
}

@BindingAdapter("dogSize")
fun TextView.bindDogSize(sizeType: String) {
    text =
        when (sizeType) {
            "소형견" -> "소형견이에요"
            "중형견" -> "중현견이에요"
            "대형견" -> "대형견이에요"
            else -> ""
        }
}

@BindingAdapter("dogBirthday")
fun TextView.binDogBirthday(birthday: LocalDate) {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM")
    val formattedDate = birthday.format(formatter)
    this.text = formattedDate
}
