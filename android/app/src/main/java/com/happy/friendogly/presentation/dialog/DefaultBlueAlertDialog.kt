package com.happy.friendogly.presentation.dialog

import android.view.View
import com.happy.friendogly.R
import com.happy.friendogly.databinding.DialogBlueDefaultAlertBinding
import com.happy.friendogly.presentation.base.BaseDialog

class DefaultBlueAlertDialog(
    private val alertDialogModel: AlertDialogModel,
    private val clickToNegative: () -> Unit,
    private val clickToPositive: () -> Unit,
) : BaseDialog<DialogBlueDefaultAlertBinding>(layoutResourceId = R.layout.dialog_blue_default_alert) {
    override fun initViewCreated() {
        initDataBinding()
        initClickListener()
    }

    private fun initDataBinding() {
        binding.model = alertDialogModel
    }

    private fun initClickListener() {
        if (alertDialogModel.title == null) {
            binding.tvAlertTitle.visibility = View.GONE
        }
        if (alertDialogModel.description == null) {
            binding.tvAlertDescription.visibility = View.GONE
        }

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
