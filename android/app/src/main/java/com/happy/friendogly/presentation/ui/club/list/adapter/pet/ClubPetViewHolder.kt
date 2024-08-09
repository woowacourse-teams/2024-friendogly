package com.happy.friendogly.presentation.ui.club.list.adapter.pet

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemClubPetImageBinding
import com.happy.friendogly.presentation.ui.club.model.ClubPet

class ClubPetViewHolder(
    private val binding: ItemClubPetImageBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(pet: ClubPet) {
        binding.pet = pet
    }
}
