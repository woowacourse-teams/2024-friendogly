package com.happy.friendogly.presentation.ui.club.list.adapter.filter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemFilterBinding
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter

class FilterViewHolder(
    private val binding: ItemFilterBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(filter: ClubFilter) {
        binding.clubFilter = filter
        binding.executePendingBindings()
    }
}
