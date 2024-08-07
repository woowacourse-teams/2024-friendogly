package com.happy.friendogly.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import com.happy.friendogly.R

@SuppressLint("UseCompatLoadingForDrawables")
fun EditText.customOnFocusChangeListener(context: Context) {
    this.onFocusChangeListener =
        View.OnFocusChangeListener { view, gainFocus ->
            if (gainFocus) {
                view.background =
                    context.getDrawable(R.drawable.rect_gray03_line_gray06_16)
            } else {
                view.background = context.getDrawable(R.drawable.rect_gray03_fill_16)
            }
        }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun EditText.messageTextOnFocusChangeListener(
    context: Context,
    linearLayout: LinearLayout,
) {
    this.onFocusChangeListener =
        View.OnFocusChangeListener { view, gainFocus ->
            if (gainFocus) {
                linearLayout.background =
                    context.getDrawable(R.drawable.rect_gray03_line_gray06_16)
            } else {
                linearLayout.background =
                    context.getDrawable(R.drawable.rect_gray03_fill_16)
            }
        }
}
