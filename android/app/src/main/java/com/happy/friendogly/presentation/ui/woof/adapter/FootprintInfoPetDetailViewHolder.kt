package com.happy.friendogly.presentation.ui.woof.adapter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemPetDetailBinding
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoPetDetailUiModel

class FootprintInfoPetDetailViewHolder(
    private val binding: ItemPetDetailBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(footprintPetDetail: FootprintInfoPetDetailUiModel) {
        binding.footprintPetDetail = footprintPetDetail
    }
}
