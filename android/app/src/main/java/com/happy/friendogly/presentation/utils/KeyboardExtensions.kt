package com.happy.friendogly.presentation.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment

fun Activity.hideKeyboard() {
    if (this.currentFocus != null) {
        val inputManager: InputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            this.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS,
        )
    }
}

fun Activity.showKeyboard(view: View) {
    val inputManager: InputMethodManager =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Fragment.hideKeyboard() {
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
}

fun Fragment.showKeyboard(view: View) {
    val inputManager: InputMethodManager =
        this.requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}
