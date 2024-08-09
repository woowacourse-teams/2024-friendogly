package com.happy.friendogly.presentation.ui.club.list.adapter.club

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemClubBinding
import com.happy.friendogly.presentation.ui.club.list.ClubListActionHandler
import com.happy.friendogly.presentation.ui.club.list.ClubListUiModel
import com.happy.friendogly.presentation.ui.club.list.adapter.filter.FilterAdapter
import com.happy.friendogly.presentation.ui.club.list.adapter.pet.ClubPetAdapter

class ClubViewHolder(
    private val binding: ItemClubBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val filterAdapter = FilterAdapter()
    private val woofAdapter = ClubPetAdapter()

    init {
        binding.rcvClubListFilterList.adapter = filterAdapter
        binding.rcvClubListDogList.adapter = woofAdapter
    }

    fun bind(
        clubListUiModel: ClubListUiModel,
        actionHandler: ClubListActionHandler,
    ) {
        binding.club = clubListUiModel
        binding.actionHandler = actionHandler
        filterAdapter.submitList(clubListUiModel.filters)
        woofAdapter.submitList(clubListUiModel.clubPets)
    }
}
