package com.happy.friendogly.presentation.ui.playground.util

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
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundActionHandler
import com.happy.friendogly.presentation.ui.playground.state.PlaygroundUiState
import com.happy.friendogly.presentation.ui.playground.uimodel.MyPlaygroundUiModel
import com.happy.friendogly.presentation.ui.playground.uimodel.PlaygroundInfoUiModel
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
fun TextView.bindMyPlaygroundBtnVisibility(uiState: PlaygroundUiState?) {
    isVisible =
        (uiState !is PlaygroundUiState.RegisteringPlayground && uiState !is PlaygroundUiState.ViewingPlaygroundSummary)
}

@BindingAdapter("playgroundLocationBtnVisibility")
fun View.bindPlaygroundLocationBtnVisibility(uiState: PlaygroundUiState?) {
    isVisible =
        (uiState !is PlaygroundUiState.RegisteringPlayground && uiState !is PlaygroundUiState.ViewingPlaygroundSummary)
}

@BindingAdapter("registeringVisibility")
fun View.bindRegisteringVisibility(uiState: PlaygroundUiState?) {
    isVisible = uiState is PlaygroundUiState.RegisteringPlayground
}

@BindingAdapter("viewingPlaygroundSummaryAnimation")
fun View.bindViewingPlaygroundSummaryAnimation(uiState: PlaygroundUiState?) {
    if (uiState is PlaygroundUiState.ViewingPlaygroundSummary) {
        showViewAnimation()
    } else {
        hideViewAnimation()
    }
}

@BindingAdapter("loadingVisibility")
fun FrameLayout.bindLoadingVisibility(uiState: PlaygroundUiState?) {
    isVisible = (uiState is PlaygroundUiState.Loading)
}

@BindingAdapter("loadingAnimation")
fun LottieAnimationView.bindLoadingAnimation(uiState: PlaygroundUiState?) {
    if (uiState is PlaygroundUiState.Loading) {
        playAnimation()
    } else {
        pauseAnimation()
    }
}

@BindingAdapter("playgroundInfoUiState", "playgroundInfoVisibility")
fun View.bindPlaygroundInfoVisibility(
    uiState: PlaygroundUiState?,
    playgroundInfo: PlaygroundInfoUiModel?,
) {
    isVisible =
        if (playgroundInfo != null && (
                uiState is PlaygroundUiState.Loading ||
                    uiState is PlaygroundUiState.FindingPlayground ||
                    uiState is PlaygroundUiState.ViewingPlaygroundInfo
            )
        ) {
            bringToFront()
            true
        } else {
            false
        }
}

@BindingAdapter("petExistenceBtnUiState")
fun View.bindPetExistenceBtnVisibility(uiState: PlaygroundUiState?) {
    isVisible =
        (uiState !is PlaygroundUiState.RegisteringPlayground && uiState !is PlaygroundUiState.ViewingPlaygroundSummary)
}

@BindingAdapter("refreshBtnUiState")
fun TextView.bindRefreshBtnVisibility(uiState: PlaygroundUiState?) {
    isVisible =
        if (uiState is PlaygroundUiState.FindingPlayground) {
            uiState.refreshBtnVisible
        } else {
            false
        }
}

@BindingAdapter("registerPlaygroundBtnUiState")
fun TextView.bindRegisterPlaygroundBtnClickable(uiState: PlaygroundUiState?) {
    isClickable =
        if (uiState is PlaygroundUiState.RegisteringPlayground) {
            uiState.playgroundRegisterBtnClickable.cameraIdle
        } else {
            false
        }
}

@BindingAdapter("btnMargin")
fun View.bindBtnMargin(myPlayground: MyPlaygroundUiModel?) {
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
fun View.bindRegisterLocationBtnVisibility(uiState: PlaygroundUiState?) {
    isVisible = (uiState is PlaygroundUiState.RegisteringPlayground)
}

@BindingAdapter("playgroundAction", "playgroundBtn", "playgroundId")
fun AppCompatButton.bindPlaygroundBtn(
    playgroundAction: PlaygroundActionHandler,
    myPlayground: MyPlaygroundUiModel?,
    playgroundId: Long,
) {
    if (myPlayground != null && myPlayground.id == playgroundId) {
        text = resources.getString(R.string.playground_leave)
        setOnClickListener {
            playgroundAction.clickLeavePlaygroundBtn()
        }
    } else {
        text = resources.getString(R.string.playground_join)
        setOnClickListener {
            playgroundAction.clickJoinPlaygroundBtn(playgroundId)
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
fun ImageButton.bindHelpBtnVisibility(uiState: PlaygroundUiState?) {
    isVisible = uiState is PlaygroundUiState.RegisteringPlayground
}

@BindingAdapter("addressText")
fun TextView.bindAddressText(uiState: PlaygroundUiState?) {
    if (uiState is PlaygroundUiState.RegisteringPlayground) text = uiState.address
}
