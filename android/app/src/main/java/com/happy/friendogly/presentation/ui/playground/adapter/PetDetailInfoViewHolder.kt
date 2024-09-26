package com.happy.friendogly.presentation.ui.playground.adapter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemPlaygroundPetDetailBinding
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundActionHandler
import com.happy.friendogly.presentation.ui.playground.uimodel.PetDetailInfoUiModel

class PetDetailInfoViewHolder(
    private val binding: ItemPlaygroundPetDetailBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        petDetailInfo: PetDetailInfoUiModel,
        actionHandler: PlaygroundActionHandler,
    ) {
        binding.petDetailInfo = petDetailInfo
        binding.actionHandler = actionHandler
    }
}
