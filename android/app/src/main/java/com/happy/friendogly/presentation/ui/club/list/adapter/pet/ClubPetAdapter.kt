package com.happy.friendogly.presentation.ui.club.list.adapter.pet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.happy.friendogly.databinding.ItemClubPetImageBinding
import com.happy.friendogly.presentation.ui.club.common.model.ClubPet

class ClubPetAdapter : ListAdapter<ClubPet, ClubPetViewHolder>(ClubPetDiffCallback()) {
    class ClubPetDiffCallback : DiffUtil.ItemCallback<ClubPet>() {
        override fun areItemsTheSame(
            oldItem: ClubPet,
            newItem: ClubPet,
        ): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }

        override fun areContentsTheSame(
            oldItem: ClubPet,
            newItem: ClubPet,
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ClubPetViewHolder {
        val binding =
            ItemClubPetImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClubPetViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ClubPetViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }
}
