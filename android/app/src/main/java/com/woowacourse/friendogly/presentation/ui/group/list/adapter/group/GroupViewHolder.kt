package com.woowacourse.friendogly.presentation.ui.group.list.adapter.group

import androidx.recyclerview.widget.RecyclerView
import com.woowacourse.friendogly.databinding.ItemGroupBinding
import com.woowacourse.friendogly.presentation.ui.group.list.GroupListActionHandler
import com.woowacourse.friendogly.presentation.ui.group.list.model.GroupUiModel

class GroupViewHolder(
    private val binding: ItemGroupBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(
        groupUiModel: GroupUiModel,
        actionHandler: GroupListActionHandler,
    ){
        binding.group = groupUiModel
        binding.actionHandler = actionHandler
    }
}
