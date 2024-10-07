package com.happy.friendogly.presentation.ui.woof.adapter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemPlaygroundPetSummaryBinding

class PetSummaryViewHolder(val binding: ItemPlaygroundPetSummaryBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(petImageUrl: String) {
        binding.petImageUrl = petImageUrl
    }
}
