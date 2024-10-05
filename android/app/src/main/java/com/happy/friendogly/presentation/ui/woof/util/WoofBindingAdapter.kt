package com.happy.friendogly.presentation.ui.woof.util

import android.content.res.Resources
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.happy.friendogly.R
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.presentation.ui.woof.action.WoofActionHandler
import com.happy.friendogly.presentation.ui.woof.state.WoofUiState
import com.happy.friendogly.presentation.ui.woof.uimodel.MyPlaygroundMarkerUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.PlaygroundInfoUiModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.Period

private const val MARGIN_BOTTOM_DEFAULT = 36
private const val MARGIN_BOTTOM_PLAYING = 108

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

@BindingAdapter("uiState")
fun View.bindMyFootprintBtnVisibility(uiState: WoofUiState?) {
    isVisible = (uiState != WoofUiState.RegisteringPlayground)
}

@BindingAdapter("registeringVisibility")
fun View.bindRegisteringVisibility(uiState: WoofUiState?) {
    isVisible = (uiState is WoofUiState.RegisteringPlayground)
}

@BindingAdapter("registeringVisibilityAnimation")
fun View.bindRegisteringVisibilityAnimation(uiState: WoofUiState?) {
    if (uiState is WoofUiState.RegisteringPlayground) {
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

@BindingAdapter("playgroundDetailVisibility")
fun View.bindPlaygroundDetailVisibility(myFootprint: MyPlaygroundMarkerUiModel?) {
    isVisible =
        if (myFootprint != null) {
            bringToFront()
            true
        } else {
            false
        }
}

@BindingAdapter("uiState", "petExistenceBtnVisibility")
fun View.bindPetExistenceBtnVisibility(
    uiState: WoofUiState?,
    myPlaygroundMarker: MyPlaygroundMarkerUiModel?,
) {
    isVisible =
        if (uiState == WoofUiState.RegisteringPlayground) {
            false
        } else {
            if (myPlaygroundMarker == null) true else false
        }
}

@BindingAdapter("uiState", "refreshBtnVisibility")
fun TextView.bindRefreshBtnVisibility(
    uiState: WoofUiState?,
    refreshBtnVisible: Boolean,
) {
    isVisible =
        if (uiState is WoofUiState.FindingPlayground) {
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
        if (uiState is WoofUiState.RegisteringPlayground) {
            cameraIdle
        } else {
            false
        }
}

@BindingAdapter("locationBtnMargin")
fun View.bindLocationBtn(myFootprint: MyPlaygroundMarkerUiModel?) {
    fun Int.dp(): Int {
        val metrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics)
            .toInt()
    }

    val marginBottom =
        if (myFootprint != null) {
            MARGIN_BOTTOM_PLAYING
        } else {
            MARGIN_BOTTOM_DEFAULT
        }

    val layoutParams = this.layoutParams as ConstraintLayout.LayoutParams
    layoutParams.apply {
        bottomMargin = marginBottom.dp()
    }

    this.layoutParams = layoutParams
}

@BindingAdapter("registerLocationBtnVisibility")
fun View.bindRegisterLocationBtnVisibility(uiState: WoofUiState?) {
    isVisible = (uiState == WoofUiState.RegisteringPlayground)
}

@BindingAdapter("playgroundAction", "playgroundBtn")
fun AppCompatButton.bindPlaygroundBtn(
    playgroundAction: WoofActionHandler,
    myFootprint: MyPlaygroundMarkerUiModel?,
) {
    if (myFootprint == null) {
        text = resources.getString(R.string.playground_participate)
        setOnClickListener {
            playgroundAction.clickParticipatePlayground()
        }
    } else {
        text = resources.getString(R.string.playground_exit)
        setOnClickListener {
            playgroundAction.clickExitPlaygroundBtn()
        }
    }
}

@BindingAdapter("playgroundBtnVisibility")
fun AppCompatButton.bindPlaygroundBtnVisibility(playgroundInfo: PlaygroundInfoUiModel?) {
    if (playgroundInfo != null) {
        visibility = if (playgroundInfo.petDetails.isNotEmpty()) View.VISIBLE else View.GONE
    }
}

@BindingAdapter("petIsArrival")
fun TextView.bindPetIsArrival(isArrival: Boolean) {
    if (isArrival) {
        text = resources.getString(R.string.playground_pet_is_playing)
        backgroundTintList = resources.getColorStateList(R.color.green400, null)
    } else {
        text = resources.getString(R.string.playground_pet_is_away)
        backgroundTintList = resources.getColorStateList(R.color.gray400, null)
    }
}

@BindingAdapter("helpBtnVisibility")
fun ImageButton.bindHelpBtnVisibility(uiState: WoofUiState?) {
    isVisible = uiState == WoofUiState.RegisteringPlayground
}
