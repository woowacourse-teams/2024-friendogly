package com.happy.friendogly.presentation.ui.woof

import android.content.res.ColorStateList
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.happy.friendogly.R
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.presentation.ui.woof.model.FilterState
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import com.happy.friendogly.presentation.ui.woof.uimodel.WalkStatusInfoUiModel
import kotlinx.datetime.LocalDate
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

@BindingAdapter("walkStatusInfo")
fun TextView.bindWalkStatusInfo(walkStatusInfo: WalkStatusInfoUiModel?) {
    if (walkStatusInfo != null) {
        val duration =
            Duration.between(
                walkStatusInfo.changedWalkStatusTime.toJavaLocalDateTime(),
                java.time.LocalDateTime.now(),
            )

        val minute = duration.toMinutes()
        when (walkStatusInfo.walkStatus) {
            WalkStatus.BEFORE -> {
                text = resources.getString(R.string.woof_walk_before, minute)
                setTextColor(resources.getColor(R.color.coral400))
            }

            WalkStatus.ONGOING -> {
                text = resources.getString(R.string.woof_walk_ongoing, minute)
                setTextColor(resources.getColor(R.color.coral500))
            }

            WalkStatus.AFTER -> {
                val afterHour = walkStatusInfo.changedWalkStatusTime.hour
                val afterMinute = walkStatusInfo.changedWalkStatusTime.minute
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
                setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(
                        R.drawable.ic_marker_before_clicked,
                        null,
                    ),
                    null,
                    null,
                    null,
                )
            }

            WalkStatus.ONGOING -> {
                text = resources.getString(R.string.woof_status_ongoing)
                setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(
                        R.drawable.ic_marker_ongoing_clicked,
                        null,
                    ),
                    null,
                    null,
                    null,
                )
            }

            WalkStatus.AFTER -> {
                text = resources.getString(R.string.woof_status_after)
                setCompoundDrawablesWithIntrinsicBounds(
                    resources.getDrawable(
                        R.drawable.ic_marker_after_clicked,
                        null,
                    ),
                    null,
                    null,
                    null,
                )
            }
        }
    }
}

@BindingAdapter("registeringVisibility")
fun View.bindRegisteringVisibility(uiState: WoofUiState?) {
    if (uiState != null) {
        isVisible = (uiState == WoofUiState.RegisteringFootprint)
    }
}

@BindingAdapter("registeringVisibilityAnimation")
fun View.bindRegisteringVisibilityAnimation(uiState: WoofUiState?) {
    if (uiState == WoofUiState.RegisteringFootprint) {
        showViewAnimation(this)
    } else {
        hideViewAnimation(this)
    }
}

@BindingAdapter("viewingVisibilityAnimation")
fun View.bindViewingVisibilityAnimation(uiState: WoofUiState?) {
    if (uiState == WoofUiState.ViewingFootprintInfo) {
        showViewAnimation(this)
    } else {
        hideViewAnimation(this)
    }
}

@BindingAdapter("loadingVisibility")
fun View.bindLoadingVisibility(uiState: WoofUiState?) {
    if (uiState != null) {
        isVisible = (uiState == WoofUiState.Loading)
    }
}

@BindingAdapter("loadingAnimation")
fun LottieAnimationView.bindLoadingAnimation(uiState: WoofUiState?) {
    if (uiState == WoofUiState.Loading) {
        showLoadingAnimation(this)
    } else {
        hideLoadingAnimation(this)
    }
}

@BindingAdapter("stateVisibility")
fun View.bindStateVisibility(uiState: WoofUiState?) {
    if (uiState != null) {
        isVisible = (uiState != WoofUiState.RegisteringFootprint)
    }
}

@BindingAdapter("layoutWalkVisibility")
fun View.bindLayoutWalkVisibility(walkStatus: WalkStatus?) {
    if (walkStatus != null) {
        isVisible = (walkStatus == WalkStatus.BEFORE || walkStatus == WalkStatus.ONGOING)
    }
}

@BindingAdapter("markBtnVisibility")
fun View.bindMarkBtnVisibility(walkStatus: WalkStatus?) {
    if (walkStatus != null) {
        isVisible = !(walkStatus == WalkStatus.BEFORE || walkStatus == WalkStatus.ONGOING)
    }
}

@BindingAdapter("filterState", "btnState")
fun TextView.bindStateBtnBackgroundTint(
    filterState: FilterState?,
    btnState: FilterState?,
) {
    if (filterState != null && btnState != null) {
        val whiteColor = resources.getColor(R.color.white, null)
        val coralColor = resources.getColor(R.color.coral50, null)

        backgroundTintList =
            ColorStateList.valueOf(if (filterState == btnState) coralColor else whiteColor)
    }
}

@BindingAdapter("refreshBtnVisibility")
fun View.bindRefreshBtnVisibility(uiState: WoofUiState?) {
    isVisible =
        if (uiState is WoofUiState.FindingFriends) {
            uiState.refreshBtnVisible
        } else {
            false
        }
}
