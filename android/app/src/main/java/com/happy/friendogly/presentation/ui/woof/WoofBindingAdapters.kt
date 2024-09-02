package com.happy.friendogly.presentation.ui.woof

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.happy.friendogly.R
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.presentation.ui.woof.model.FilterState
import com.happy.friendogly.presentation.ui.woof.model.FootprintRecentWalkStatus
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import com.happy.friendogly.presentation.ui.woof.uimodel.WalkStatusInfoUiModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import java.time.Duration
import java.time.Period

private const val MARGIN_BOTTOM_DEFAULT = 36
private const val MARGIN_BOTTOM_REGISTERING_FOOTPRINT = 12
private const val MARGIN_BOTTOM_ON_WALKING = 12

@BindingAdapter("memberName")
fun TextView.bindMemberName(memberName: String) {
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
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.coral500)),
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

@BindingAdapter("petAge")
fun TextView.bindPetAge(petBirthDate: LocalDate) {
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

@BindingAdapter("petSizeType")
fun TextView.bindPetSizeType(petSizeType: SizeType) {
    text =
        when (petSizeType) {
            SizeType.SMALL -> resources.getString(R.string.dog_small)
            SizeType.MEDIUM -> resources.getString(R.string.dog_medium)
            SizeType.LARGE -> resources.getString(R.string.dog_large)
        }
}

@BindingAdapter("petGender")
fun TextView.bindPetGender(petGender: Gender) {
    text =
        when (petGender) {
            Gender.MALE -> resources.getString(R.string.dog_gender_male)
            Gender.FEMALE -> resources.getString(R.string.dog_gender_female)
            Gender.MALE_NEUTERED -> resources.getString(R.string.dog_gender_male_neutered)
            Gender.FEMALE_NEUTERED -> resources.getString(R.string.dog_gender_female_neutered)
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
                setTextColor(ContextCompat.getColor(context, R.color.coral400))
            }

            WalkStatus.ONGOING -> {
                text = resources.getString(R.string.woof_walk_ongoing, minute)
                setTextColor(ContextCompat.getColor(context, R.color.coral500))
            }

            WalkStatus.AFTER -> {
                val afterHour = walkStatusInfo.changedWalkStatusTime.hour
                val afterMinute = walkStatusInfo.changedWalkStatusTime.minute
                text = resources.getString(R.string.woof_walk_after, afterHour, afterMinute)
                setTextColor(ContextCompat.getColor(context, R.color.gray600))
            }
        }
    }
}

@BindingAdapter("myWalkStatus")
fun TextView.bindMyWalkStatus(walkStatus: WalkStatus?) {
    if (walkStatus != null) {
        val drawable: Drawable? =
            when (walkStatus) {
                WalkStatus.BEFORE ->
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_marker_before_clicked,
                    )

                WalkStatus.ONGOING ->
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_marker_ongoing_clicked,
                    )

                WalkStatus.AFTER ->
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_marker_after_clicked,
                    )
            }
        text =
            when (walkStatus) {
                WalkStatus.BEFORE -> context.getString(R.string.woof_status_before)
                WalkStatus.ONGOING -> context.getString(R.string.woof_status_ongoing)
                WalkStatus.AFTER -> context.getString(R.string.woof_status_after)
            }
        setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    }
}

@BindingAdapter("findFriendsVisibility")
fun View.bindFindFriendsVisibility(uiState: WoofUiState?) {
    isVisible =
        (uiState is WoofUiState.Loading || uiState is WoofUiState.FindingFriends || uiState is WoofUiState.LocationPermissionsNotGranted)
}

@BindingAdapter("registeringVisibility")
fun View.bindRegisteringVisibility(uiState: WoofUiState?) {
    isVisible = (uiState is WoofUiState.RegisteringFootprint)
}

@BindingAdapter("registeringVisibilityAnimation")
fun View.bindRegisteringVisibilityAnimation(uiState: WoofUiState?) {
    if (uiState is WoofUiState.RegisteringFootprint) {
        showViewAnimation()
    } else {
        hideViewAnimation()
    }
}

@BindingAdapter("viewingVisibilityAnimation")
fun View.bindViewingVisibilityAnimation(uiState: WoofUiState?) {
    if (uiState == WoofUiState.ViewingFootprintInfo) {
        showViewAnimation()
    } else {
        hideViewAnimation()
    }
}

@BindingAdapter("loadingVisibility")
fun FrameLayout.bindLoadingVisibility(uiState: WoofUiState?) {
    isVisible = (uiState == WoofUiState.Loading)
}

@BindingAdapter("loadingAnimation")
fun LottieAnimationView.bindLoadingAnimation(uiState: WoofUiState?) {
    if (uiState == WoofUiState.Loading) {
        playAnimation()
    } else {
        pauseAnimation()
    }
}

@BindingAdapter("stateVisibility")
fun View.bindStateVisibility(uiState: WoofUiState?) {
    isVisible = (uiState !is WoofUiState.RegisteringFootprint)
}

@BindingAdapter("uiState", "myWalkStatusVisibility")
fun View.bindMyWalkStatusVisibility(
    uiState: WoofUiState?,
    myWalkStatus: FootprintRecentWalkStatus?,
) {
    isVisible =
        if (uiState == WoofUiState.FindingFriends) {
            (myWalkStatus?.walkStatus == WalkStatus.BEFORE || myWalkStatus?.walkStatus == WalkStatus.ONGOING)
        } else {
            false
        }
}

@BindingAdapter("markBtnVisibility")
fun View.bindMarkBtnVisibility(myWalkStatus: FootprintRecentWalkStatus?) {
    isVisible =
        if (myWalkStatus != null) {
            !(myWalkStatus.walkStatus == WalkStatus.BEFORE || myWalkStatus.walkStatus == WalkStatus.ONGOING)
        } else {
            true
        }
}

@BindingAdapter("filterState", "btnState")
fun TextView.bindFilterStateBtnBackgroundTint(
    filterState: FilterState,
    btnState: FilterState,
) {
    val whiteColor = resources.getColor(R.color.white, null)
    val coralColor = resources.getColor(R.color.coral50, null)

    backgroundTintList =
        ColorStateList.valueOf(if (filterState == btnState) coralColor else whiteColor)
}

@BindingAdapter("uiState", "refreshBtnVisibility")
fun TextView.bindRefreshBtnVisibility(
    uiState: WoofUiState?,
    refreshBtnVisible: Boolean,
) {
    isVisible =
        if (uiState is WoofUiState.FindingFriends) {
            refreshBtnVisible
        } else {
            false
        }
}

@BindingAdapter("uiState", "cameraIdle")
fun TextView.bindRegisterFootprintBtnClickable(
    uiState: WoofUiState?,
    cameraIdle: Boolean,
) {
    isClickable =
        if (uiState is WoofUiState.RegisteringFootprint) {
            cameraIdle
        } else {
            false
        }
}

@BindingAdapter("deleteMyFootprintBtnVisibility")
fun ImageButton.bindDeleteMyFootprintBtnVisibility(walkStatus: WalkStatus?) {
    if (walkStatus != null) {
        isVisible = (walkStatus == WalkStatus.BEFORE)
    }
}

@BindingAdapter("endWalkBtnVisibility")
fun ImageButton.bindEndWalkBtnVisibility(walkStatus: WalkStatus?) {
    if (walkStatus != null) {
        isVisible = (walkStatus == WalkStatus.ONGOING)
    }
}

@BindingAdapter("uiState", "locationBtn")
fun View.bindLocationBtn(
    uiState: WoofUiState?,
    myWalkStatus: FootprintRecentWalkStatus?,
) {
    fun Int.dp(): Int {
        val metrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics)
            .toInt()
    }

    val marginBottom =
        if (uiState is WoofUiState.RegisteringFootprint) {
            MARGIN_BOTTOM_REGISTERING_FOOTPRINT
        } else {
            if (myWalkStatus != null) {
                if (myWalkStatus.walkStatus == WalkStatus.AFTER) MARGIN_BOTTOM_DEFAULT else MARGIN_BOTTOM_ON_WALKING
            } else {
                MARGIN_BOTTOM_DEFAULT
            }
        }

    val layoutParams = this.layoutParams as ConstraintLayout.LayoutParams
    layoutParams.apply {
        bottomToTop =
            if (uiState is WoofUiState.RegisteringFootprint) {
                R.id.layout_woof_register_marker
            } else {
                R.id.layout_woof_walk
            }
        bottomMargin = marginBottom.dp()
    }

    this.layoutParams = layoutParams
}

@BindingAdapter("uiState", "helpBtn")
fun ImageButton.bindHelpBtn(
    uiState: WoofUiState?,
    myWalkStatus: FootprintRecentWalkStatus?,
) {
    isVisible =
        when (uiState) {
            WoofUiState.FindingFriends -> {
                (myWalkStatus?.walkStatus == WalkStatus.BEFORE || myWalkStatus?.walkStatus == WalkStatus.ONGOING)
            }

            WoofUiState.RegisteringFootprint -> {
                true
            }

            else -> {
                false
            }
        }

    val layoutParams = this.layoutParams as ConstraintLayout.LayoutParams
    layoutParams.apply {
        bottomToTop =
            if (uiState is WoofUiState.RegisteringFootprint) {
                R.id.layout_woof_register_marker
            } else {
                R.id.layout_woof_walk
            }
    }

    this.layoutParams = layoutParams
}
