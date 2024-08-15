package com.happy.friendogly.presentation.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.happy.friendogly.R

class LoadingDialog(context: Context) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_loading)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
        window?.setBackgroundDrawable(ColorDrawable())
        window?.setDimAmount(0.5f)
    }

    override fun show() {
        if (!this.isShowing) super.show()
    }
}
