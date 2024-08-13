package com.happy.friendogly.presentation.ui.club.select.adapter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemClubSelectProfileBinding
import com.happy.friendogly.presentation.ui.club.select.PetSelectActionHandler
import com.happy.friendogly.presentation.ui.club.select.PetSelectUiModel

class PetSelectViewHolder(
    private val binding: ItemClubSelectProfileBinding,
    private val actionHandler: PetSelectActionHandler,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(petSelectUiModel: PetSelectUiModel) {
        binding.dogUiModel = petSelectUiModel
        binding.dogSelectActionHandler = actionHandler
    }
}
