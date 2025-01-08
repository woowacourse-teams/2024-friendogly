package com.happy.friendogly.presentation.ui.club.detail.adapter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemDetailProfileBinding
import com.happy.friendogly.presentation.ui.club.detail.ClubDetailNavigation
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailProfileUiModel

class DetailProfileViewHolder(
    private val binding: ItemDetailProfileBinding,
    private val navigation: ClubDetailNavigation,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(profile: ClubDetailProfileUiModel) {
        binding.profile = profile
        binding.navigation = navigation
    }
}
