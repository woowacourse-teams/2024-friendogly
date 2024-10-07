package com.happy.friendogly.presentation.ui.woof.util

import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.happy.friendogly.R
import com.happy.friendogly.domain.model.Gender
import com.happy.friendogly.domain.model.SizeType
import com.happy.friendogly.presentation.ui.woof.action.WoofActionHandler
import com.happy.friendogly.presentation.ui.woof.state.WoofUiState
import com.happy.friendogly.presentation.ui.woof.uimodel.PlaygroundInfoUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.PlaygroundMarkerUiModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.Period

private const val MARGIN_BOTTOM_DEFAULT = 26
private const val MARGIN_BOTTOM_PLAYING = 106

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

@BindingAdapter("myPlaygroundBtnVisibility")
fun TextView.bindMyPlaygroundBtnVisibility(uiState: WoofUiState?) {
    isVisible =
        (uiState != WoofUiState.RegisteringPlayground && uiState != WoofUiState.ViewingPlaygroundSummary)
}

@BindingAdapter("playgroundLocationBtnVisibility")
fun View.bindPlaygroundLocationBtnVisibility(uiState: WoofUiState?) {
    isVisible =
        (uiState != WoofUiState.RegisteringPlayground && uiState != WoofUiState.ViewingPlaygroundSummary)
}

@BindingAdapter("registeringVisibility")
fun View.bindRegisteringVisibility(uiState: WoofUiState?) {
    isVisible = (uiState is WoofUiState.RegisteringPlayground)
}

@BindingAdapter("registeringPlaygroundAnimation")
fun View.bindRegisteringAnimation(uiState: WoofUiState?) {
    if (uiState is WoofUiState.RegisteringPlayground) {
        showViewAnimation()
    } else {
        hideViewAnimation()
    }
}

@BindingAdapter("viewingPlaygroundSummaryAnimation")
fun View.bindViewingPlaygroundSummaryAnimation(uiState: WoofUiState?) {
    if (uiState is WoofUiState.ViewingPlaygroundSummary) {
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

@BindingAdapter("uiState", "playgroundInfoVisibility")
fun View.bindPlaygroundInfoVisibility(
    uiState: WoofUiState?,
    myPlayground: PlaygroundMarkerUiModel?,
) {
    isVisible =
        if (myPlayground != null && (uiState == WoofUiState.FindingPlayground || uiState == WoofUiState.ViewingPlaygroundInfo)) {
            bringToFront()
            true
        } else {
            false
        }
}

@BindingAdapter("uiState", "petExistenceBtnVisibility")
fun View.bindPetExistenceBtnVisibility(
    uiState: WoofUiState?,
    myPlaygroundMarker: PlaygroundMarkerUiModel?,
) {
    isVisible =
        if (uiState == WoofUiState.RegisteringPlayground) {
            false
        } else {
            myPlaygroundMarker == null
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
fun TextView.bindRegisterPlaygroundBtnClickable(
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
fun View.bindLocationBtn(myPlayground: PlaygroundMarkerUiModel?) {
    fun Int.dp(): Int {
        val metrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics)
            .toInt()
    }

    val marginBottom =
        if (myPlayground != null) {
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
    myPlayground: PlaygroundMarkerUiModel?,
) {
    if (myPlayground == null) {
        text = resources.getString(R.string.playground_participate)
        setOnClickListener {
            playgroundAction.clickParticipatePlaygroundBtn()
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
