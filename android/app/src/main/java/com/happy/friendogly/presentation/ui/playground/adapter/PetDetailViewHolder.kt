package com.happy.friendogly.presentation.ui.playground.adapter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemPlaygroundPetDetailBinding
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundActionHandler
import com.happy.friendogly.presentation.ui.playground.uimodel.PlaygroundPetDetailUiModel

class PetDetailViewHolder(
    private val binding: ItemPlaygroundPetDetailBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        petDetail: PlaygroundPetDetailUiModel,
        actionHandler: PlaygroundActionHandler,
    ) {
        binding.petDetail = petDetail
        binding.actionHandler = actionHandler
    }
}
