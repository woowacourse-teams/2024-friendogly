package com.happy.friendogly.presentation.ui.playground.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.isVisible

const val ANIMATE_DURATION_MILLIS = 300L

fun View.showViewAnimation() {
    if (height == 0) {
        viewTreeObserver.addOnGlobalLayoutListener(
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    animateView()
                    if (height > 0) viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            },
        )
    } else {
        animateView()
    }
}

private fun View.animateView() {
    translationY = height.toFloat()
    animate().translationY(0f).setDuration(ANIMATE_DURATION_MILLIS).setListener(
        object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                isVisible = true
            }
        },
    )
}

fun View.hideViewAnimation() {
    animate().translationY(height.toFloat()).setDuration(ANIMATE_DURATION_MILLIS)
        .setListener(
            object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    isVisible = false
                }
            },
        )
}
