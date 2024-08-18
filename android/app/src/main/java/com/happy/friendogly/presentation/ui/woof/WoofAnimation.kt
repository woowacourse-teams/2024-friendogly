package com.happy.friendogly.presentation.ui.woof

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView

const val ANIMATE_DURATION_MILLIS = 300L
private const val INVISIBLE_VIEW_HEIGHT = 900.0f

fun showLoadingAnimation(loadingAnimationView: LottieAnimationView) {
    loadingAnimationView.playAnimation()
}

fun hideLoadingAnimation(loadingAnimationView: LottieAnimationView) {
    loadingAnimationView.pauseAnimation()
}

fun showViewAnimation(view: View) {
    view.apply {
        translationY = if (height != 0) height.toFloat() else INVISIBLE_VIEW_HEIGHT
        animate().translationY(0f).setDuration(ANIMATE_DURATION_MILLIS).setListener(
            object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    isVisible = true
                    bringToFront()
                }
            },
        )
    }
}

fun hideViewAnimation(view: View) {
    view.animate().translationY(view.height.toFloat()).setDuration(ANIMATE_DURATION_MILLIS)
        .setListener(
            object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    view.isVisible = false
                }
            },
        )
}
