package com.happy.friendogly.presentation.ui.group.list.adapter.group

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemGroupBinding
import com.happy.friendogly.presentation.ui.group.list.GroupListActionHandler
import com.happy.friendogly.presentation.ui.group.list.GroupListUiModel
import com.happy.friendogly.presentation.ui.group.list.adapter.filter.FilterAdapter
import com.happy.friendogly.presentation.ui.group.list.adapter.woof.GroupWoofAdapter

class GroupViewHolder(
    private val binding: ItemGroupBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val filterAdapter = FilterAdapter()
    private val woofAdapter = GroupWoofAdapter()

    init {
        binding.rcvGroupListFilterList.adapter = filterAdapter
        binding.rcvGroupListDogList.adapter = woofAdapter
    }

    fun bind(
        groupListUiModel: GroupListUiModel,
        actionHandler: GroupListActionHandler,
    ) {
        binding.group = groupListUiModel
        binding.actionHandler = actionHandler
        filterAdapter.submitList(groupListUiModel.filters)
        woofAdapter.submitList(groupListUiModel.groupWoofs)
    }
}
