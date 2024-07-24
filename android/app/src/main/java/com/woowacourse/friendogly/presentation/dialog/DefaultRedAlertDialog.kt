package com.woowacourse.friendogly.presentation.dialog

import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.DialogRedDefaultAlertBinding
import com.woowacourse.friendogly.presentation.base.BaseDialog

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
