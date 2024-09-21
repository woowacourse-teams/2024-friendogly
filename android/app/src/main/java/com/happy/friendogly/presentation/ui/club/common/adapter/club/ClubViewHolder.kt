package com.happy.friendogly.presentation.ui.club.common.adapter.club

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemClubBinding
import com.happy.friendogly.presentation.ui.club.common.ClubItemActionHandler
import com.happy.friendogly.presentation.ui.club.common.model.ClubItemUiModel
import com.happy.friendogly.presentation.ui.club.list.adapter.filter.FilterAdapter
import com.happy.friendogly.presentation.ui.club.list.adapter.pet.ClubPetAdapter

class ClubViewHolder(
    private val binding: ItemClubBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val woofAdapter = ClubPetAdapter()

    init {
        binding.rcvClubListDogList.adapter = woofAdapter
    }

    fun bind(
        clubItemUiModel: ClubItemUiModel,
        actionHandler: ClubItemActionHandler,
    ) {
        binding.club = clubItemUiModel
        binding.actionHandler = actionHandler
        woofAdapter.submitList(clubItemUiModel.clubPets)
    }
}
