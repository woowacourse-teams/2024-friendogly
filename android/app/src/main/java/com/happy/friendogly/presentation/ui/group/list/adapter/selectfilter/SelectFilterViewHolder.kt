package com.happy.friendogly.presentation.ui.group.list.adapter.selectfilter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemSelectFilterBinding
import com.happy.friendogly.presentation.ui.group.list.GroupListActionHandler
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

class SelectFilterViewHolder(
    private val binding: ItemSelectFilterBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(
        filter: GroupFilter,
        actionHandler: GroupListActionHandler,
    ) {
        binding.groupFilter = filter
        binding.actionHandler = actionHandler
        binding.executePendingBindings()
    }
}
