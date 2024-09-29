package com.happy.friendogly.presentation.ui.woof.adapter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemPlaygroundFirstPetDetailBinding
import com.happy.friendogly.presentation.ui.woof.action.WoofActionHandler
import com.happy.friendogly.presentation.ui.woof.uimodel.PetDetailInfoUiModel

class FirstPetDetailViewHolder(
    private val binding: ItemPlaygroundFirstPetDetailBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        petDetailInfo: PetDetailInfoUiModel,
        actionHandler: WoofActionHandler,
    ) {
        binding.petDetailInfo = petDetailInfo
        binding.actionHandler = actionHandler
    }
}
