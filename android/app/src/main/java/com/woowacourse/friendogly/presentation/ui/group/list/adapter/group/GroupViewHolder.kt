package com.woowacourse.friendogly.presentation.ui.group.list.adapter.group

import androidx.recyclerview.widget.RecyclerView
import com.woowacourse.friendogly.databinding.ItemGroupBinding
import com.woowacourse.friendogly.presentation.ui.group.list.GroupListActionHandler
import com.woowacourse.friendogly.presentation.ui.group.list.adapter.filter.FilterAdapter
import com.woowacourse.friendogly.presentation.ui.group.list.adapter.woof.GroupWoofAdapter
import com.woowacourse.friendogly.presentation.ui.group.list.GroupListUiModel

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
