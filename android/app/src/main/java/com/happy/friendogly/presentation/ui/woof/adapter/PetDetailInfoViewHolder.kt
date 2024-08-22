package com.happy.friendogly.presentation.ui.woof.adapter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemFootprintInfoPetDetailBinding
import com.happy.friendogly.presentation.ui.woof.WoofActionHandler
import com.happy.friendogly.presentation.ui.woof.uimodel.PetDetailInfoUiModel

class PetDetailInfoViewHolder(
    private val binding: ItemFootprintInfoPetDetailBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        petDetailInfo: PetDetailInfoUiModel,
        actionHandler: WoofActionHandler,
    ) {
        binding.petDetailInfo = petDetailInfo
        binding.actionHandler = actionHandler
    }
}
