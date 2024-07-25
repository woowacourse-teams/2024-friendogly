package com.happy.friendogly.presentation.ui.group.list.adapter.group

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.happy.friendogly.databinding.ItemGroupBinding
import com.happy.friendogly.presentation.ui.group.list.GroupListActionHandler
import com.happy.friendogly.presentation.ui.group.list.GroupListUiModel

class GroupListAdapter(
    private val actionHandler: GroupListActionHandler,
) : ListAdapter<GroupListUiModel, GroupViewHolder>(GroupDiffCallback()) {
    class GroupDiffCallback : DiffUtil.ItemCallback<GroupListUiModel>() {
        override fun areItemsTheSame(
            oldItem: GroupListUiModel,
            newItem: GroupListUiModel,
        ): Boolean {
            return oldItem.groupId == newItem.groupId
        }

        override fun areContentsTheSame(
            oldItem: GroupListUiModel,
            newItem: GroupListUiModel,
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
