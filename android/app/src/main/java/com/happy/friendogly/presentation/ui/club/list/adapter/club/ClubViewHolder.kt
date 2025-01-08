package com.happy.friendogly.presentation.ui.club.list.adapter.club

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemClubBinding
import com.happy.friendogly.presentation.ui.club.common.ClubItemActionHandler
import com.happy.friendogly.presentation.ui.club.common.model.ClubItemUiModel
import com.happy.friendogly.presentation.ui.club.list.adapter.pet.ClubPetAdapter

class ClubViewHolder(
    private val binding: ItemClubBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val clubPetAdapter = ClubPetAdapter()

    init {
        binding.rcvClubListDogList.adapter = clubPetAdapter
    }

    fun bind(
        clubItemUiModel: ClubItemUiModel,
        actionHandler: ClubItemActionHandler,
    ) {
        binding.club = clubItemUiModel
        binding.actionHandler = actionHandler
        clubPetAdapter.submitList(clubItemUiModel.clubPets)
    }
}
