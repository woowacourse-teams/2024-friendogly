package com.happy.friendogly.presentation.ui.woof.adapter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemPetDetailBinding
import com.happy.friendogly.presentation.ui.woof.uimodel.PetDetailUiModel

class PetDetailViewHolder(
    private val binding: ItemPetDetailBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        memberName: String,
        petDetail: PetDetailUiModel,
    ) {
        binding.memberName = memberName
        binding.petDetail = petDetail
    }
}
