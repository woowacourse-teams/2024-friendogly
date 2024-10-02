package com.happy.friendogly.presentation.ui.woof.adapter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemPlaygroundPetDetailBinding
import com.happy.friendogly.presentation.ui.woof.action.WoofActionHandler
import com.happy.friendogly.presentation.ui.woof.uimodel.PlaygroundPetDetailUiModel

class PetDetailViewHolder(
    private val binding: ItemPlaygroundPetDetailBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        petDetail: PlaygroundPetDetailUiModel,
        actionHandler: WoofActionHandler,
    ) {
        binding.petDetail = petDetail
        binding.actionHandler = actionHandler
    }
}
