package com.woowacourse.friendogly.presentation.ui.group.list.adapter.group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.woowacourse.friendogly.databinding.ItemGroupBinding
import com.woowacourse.friendogly.presentation.ui.group.list.GroupListActionHandler
import com.woowacourse.friendogly.presentation.ui.group.model.GroupUiModel

class GroupListAdapter(
    private val actionHandler: GroupListActionHandler,
) : ListAdapter<GroupUiModel, GroupViewHolder>(GroupDiffCallback()) {
    class GroupDiffCallback : DiffUtil.ItemCallback<GroupUiModel>() {
        override fun areItemsTheSame(
            oldItem: GroupUiModel,
            newItem: GroupUiModel,
        ): Boolean {
            return oldItem.groupId == newItem.groupId
        }

        override fun areContentsTheSame(
            oldItem: GroupUiModel,
            newItem: GroupUiModel,
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): GroupViewHolder {
        val binding = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: GroupViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position), actionHandler)
    }
}
