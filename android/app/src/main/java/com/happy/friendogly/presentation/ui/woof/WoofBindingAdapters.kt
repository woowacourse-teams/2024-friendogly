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
import com.happy.friendogly.domain.model.WalkStatus
import com.happy.friendogly.presentation.model.FootprintInfoUiModel
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
                days > 0 -> context.getString(R.string.woof_days_ago, days)
                hours > 0 -> context.getString(R.string.woof_hours_ago, hours)
                minutes > 0 -> context.getString(R.string.woof_minutes_ago, minutes)
                else -> context.getString(R.string.woof_just_now)
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

@BindingAdapter("walkStatus")
fun TextView.bindWalkStatus(footPrintInfo: FootprintInfoUiModel?) {
    if (footPrintInfo != null) {
        text =
            when (footPrintInfo.walkStatus) {
                WalkStatus.BEFORE -> {
                    val duration =
                        Duration.between(
                            footPrintInfo.createdAt.toJavaLocalDateTime(),
                            java.time.LocalDateTime.now(),
                        )
                    val minutes = duration.toMinutes()

                    resources.getString(R.string.woof_walk_before, minutes)
                }

                WalkStatus.ONGOING -> {
                    if (footPrintInfo.startWalkTime != null) {
                        val duration =
                            Duration.between(
                                footPrintInfo.startWalkTime.toJavaLocalDateTime(),
                                java.time.LocalDateTime.now(),
                            )
                        val minutes = duration.toMinutes()

                        resources.getString(R.string.woof_walk_ongoing, minutes)
                    }
                    resources.getString(R.string.woof_walk_unknown)
                }

                WalkStatus.AFTER -> {
                    if (footPrintInfo.endWalkTime != null) {
                        val hour = footPrintInfo.endWalkTime.hour
                        val minute = footPrintInfo.endWalkTime.minute

                        resources.getString(R.string.woof_walk_after, hour, minute)
                    }
                    resources.getString(R.string.woof_walk_unknown)
                }
            }
    }
}
