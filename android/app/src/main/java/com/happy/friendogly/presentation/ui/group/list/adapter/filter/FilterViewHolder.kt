package com.happy.friendogly.presentation.ui.group.list.adapter.filter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemFilterBinding
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

class FilterViewHolder(
    private val binding: ItemFilterBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(filter: GroupFilter) {
        binding.groupFilter = filter
        binding.executePendingBindings()
    }
}
