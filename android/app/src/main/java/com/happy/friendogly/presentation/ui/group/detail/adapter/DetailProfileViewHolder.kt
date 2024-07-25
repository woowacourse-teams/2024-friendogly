package com.happy.friendogly.presentation.ui.group.detail.adapter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemDetailProfileBinding
import com.happy.friendogly.presentation.ui.group.detail.model.GroupDetailProfileUiModel

class DetailProfileViewHolder(
    private val binding: ItemDetailProfileBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(profile: GroupDetailProfileUiModel) {
        binding.profile = profile
    }
}
