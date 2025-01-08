package com.happy.friendogly.presentation.ui.playground.adapter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemPlaygroundMyPetDetailBinding
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundActionHandler
import com.happy.friendogly.presentation.ui.playground.uimodel.PlaygroundPetDetailUiModel

class MyPetDetailViewHolder(
    private val binding: ItemPlaygroundMyPetDetailBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        petDetail: PlaygroundPetDetailUiModel,
        actionHandler: PlaygroundActionHandler,
    ) {
        binding.petDetail = petDetail
        binding.actionHandler = actionHandler
    }
}
