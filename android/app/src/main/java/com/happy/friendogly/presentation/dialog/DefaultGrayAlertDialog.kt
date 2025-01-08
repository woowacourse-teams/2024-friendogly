package com.happy.friendogly.presentation.dialog

import com.happy.friendogly.R
import com.happy.friendogly.databinding.DialogGrayDefaultAlertBinding
import com.happy.friendogly.presentation.base.BaseDialog

class DefaultGrayAlertDialog(
    private val alertDialogModel: AlertDialogModel,
    private val clickToNegative: () -> Unit,
) : BaseDialog<DialogGrayDefaultAlertBinding>(layoutResourceId = R.layout.dialog_gray_default_alert) {
    override fun initViewCreated() {
        initDataBinding()
        initClickListener()
    }

    private fun initDataBinding() {
        binding.model = alertDialogModel
    }

    private fun initClickListener() {
        binding.tvNegative.setOnClickListener {
            clickToNegative()
            dismiss()
        }
    }
}
