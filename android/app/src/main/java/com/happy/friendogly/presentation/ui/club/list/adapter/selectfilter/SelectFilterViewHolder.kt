package com.happy.friendogly.presentation.ui.club.list.adapter.selectfilter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemSelectedFilterMenuBinding
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.list.ClubListActionHandler

class SelectFilterViewHolder(
    private val binding: ItemSelectedFilterMenuBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        filter: ClubFilter,
        actionHandler: ClubListActionHandler,
    ) {
        binding.clubFilter = filter
        binding.actionHandler = actionHandler
        binding.executePendingBindings()
    }
}
