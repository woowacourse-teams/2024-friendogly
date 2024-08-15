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
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
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
                resources.getString(R.string.woof_age_month, months)
            } else {
                resources.getString(R.string.woof_age_year, years)
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

@BindingAdapter("walkStatus", "changedWalkStatusTime")
fun TextView.bindWalkStatusTime(
    walkStatus: WalkStatus?,
    changedWalkStatusTime: LocalDateTime?,
) {
    if (walkStatus != null && changedWalkStatusTime != null) {
        val duration =
            Duration.between(
                changedWalkStatusTime.toJavaLocalDateTime(),
                java.time.LocalDateTime.now(),
            )

        val minute = duration.toMinutes()
        when (walkStatus) {
            WalkStatus.BEFORE -> {
                text = resources.getString(R.string.woof_walk_before, minute)
                setTextColor(resources.getColor(R.color.coral400))
            }

            WalkStatus.ONGOING -> {
                text = resources.getString(R.string.woof_walk_ongoing, minute)
                setTextColor(resources.getColor(R.color.coral500))
            }

            WalkStatus.AFTER -> {
                val afterHour = changedWalkStatusTime.hour
                val afterMinute = changedWalkStatusTime.minute
                text = resources.getString(R.string.woof_walk_after, afterHour, afterMinute)
                setTextColor(resources.getColor(R.color.gray600))
            }
        }
    }
}

@BindingAdapter("myWalkStatus")
fun TextView.bindMyWalkStatus(walkStatus: WalkStatus?) {
    if (walkStatus != null) {
        when (walkStatus) {
            WalkStatus.BEFORE -> {
                text = resources.getString(R.string.woof_status_before)
                setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_marker_before_clicked, null), null, null, null)
            }

            WalkStatus.ONGOING -> {
                text = resources.getString(R.string.woof_status_ongoing)
                setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_marker_ongoing_clicked, null), null, null, null)
            }

            WalkStatus.AFTER -> {
                text = resources.getString(R.string.woof_status_after)
                setCompoundDrawablesWithIntrinsicBounds(resources.getDrawable(R.drawable.ic_marker_after_clicked, null), null, null, null)
            }
        }
    }
}
