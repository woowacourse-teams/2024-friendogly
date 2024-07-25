package com.woowacourse.friendogly.presentation.dialog

import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.DialogGrayDefaultAlertBinding
import com.woowacourse.friendogly.presentation.base.BaseDialog

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
