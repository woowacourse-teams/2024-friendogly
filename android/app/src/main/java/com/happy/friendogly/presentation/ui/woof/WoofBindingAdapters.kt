package com.happy.friendogly.presentation.ui.woof

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.happy.friendogly.R
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import java.time.Duration
import java.time.Period

@BindingAdapter("memberName")
fun TextView.bindMemberName(memberName: String?) {
    if (memberName != null) {
        val spannableString =
            SpannableString(
                String.format(
                    resources.getString(R.string.woof_member_name),
                    memberName,
                ),
            )
        val memberNameLength = memberName.length
        spannableString.apply {
            setSpan(
                ForegroundColorSpan(resources.getColor(R.color.coral500)),
                0,
                memberNameLength,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
            setSpan(
                UnderlineSpan(),
                0,
                memberNameLength,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE,
            )
        }

        text = spannableString
    }
}

@BindingAdapter("petAge")
fun TextView.bindPetAge(petBirthDate: LocalDate?) {
    if (petBirthDate != null) {
        val period = Period.between(petBirthDate.toJavaLocalDate(), java.time.LocalDate.now())
        val years = period.years
        val months = period.months

        text =
            if (years < 1) {
                "${months}개월"
            } else {
                "${years}살"
            }
    }
}

@BindingAdapter("createdAt")
fun TextView.bindingCreatedAt(createdAt: LocalDateTime?) {
    if (createdAt != null) {
        val duration =
            Duration.between(createdAt.toJavaLocalDateTime(), java.time.LocalDateTime.now())

        val minutes = duration.toMinutes()
        val hours = duration.toHours()
        val days = duration.toDays()

        text =
            when {
                days > 0 -> "${days}일 전"
                hours > 0 -> "${hours}시간 전"
                minutes > 0 -> "${minutes}분 전"
                else -> "방금 전"
            }
    }
}

@BindingAdapter("petSizeType")
fun TextView.bindPetSizeType(petSizeType: SizeType?) {
    if (petSizeType != null) {
        text =
            when (petSizeType) {
                SizeType.SMALL -> resources.getString(R.string.dog_small)
                SizeType.MEDIUM -> resources.getString(R.string.dog_medium)
                SizeType.LARGE -> resources.getString(R.string.dog_large)
            }
    }
}

@BindingAdapter("petGender")
fun TextView.bindPetGender(petGender: Gender?) {
    if (petGender != null) {
        text =
            when (petGender) {
                Gender.MALE -> resources.getString(R.string.dog_gender_male)
                Gender.FEMALE -> resources.getString(R.string.dog_gender_female)
                Gender.MALE_NEUTERED -> resources.getString(R.string.dog_gender_male_neutered)
                Gender.FEMALE_NEUTERED -> resources.getString(R.string.dog_gender_female_neutered)
            }
    }
}
