package com.happy.friendogly.presentation.dialog

import com.happy.friendogly.R
import com.happy.friendogly.databinding.DialogRedDefaultAlertBinding
import com.happy.friendogly.presentation.base.BaseDialog

class DefaultRedAlertDialog(
    private val alertDialogModel: AlertDialogModel,
    private val clickToNegative: () -> Unit,
    private val clickToPositive: () -> Unit,
) : BaseDialog<DialogRedDefaultAlertBinding>(layoutResourceId = R.layout.dialog_red_default_alert) {
    override fun initViewCreated() {
        initDataBinding()
        initClickListener()
    }

    fun initDataBinding() {
        binding.model = alertDialogModel
    }

    private fun initClickListener() {
        binding.tvNegative.setOnClickListener {
            clickToNegative()
            dismiss()
        }
        binding.tvPositive.setOnClickListener {
            clickToPositive()
            dismiss()
        }
    }
}
