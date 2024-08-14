package com.happy.friendogly.presentation.dialog

import com.happy.friendogly.R
import com.happy.friendogly.databinding.DialogPetAddAlertBinding
import com.happy.friendogly.presentation.base.BaseDialog

class PetAddAlertDialog(
    private val clickToNegative: () -> Unit,
    private val clickToPositive: () -> Unit,
):BaseDialog<DialogPetAddAlertBinding>(layoutResourceId = R.layout.dialog_pet_add_alert) {
    override fun initViewCreated() {
        initClickListener()
    }

    private fun initClickListener(){
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
