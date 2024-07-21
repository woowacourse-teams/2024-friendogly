package com.woowacourse.friendogly.presentation.ui.group.list.adapter.woof

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.woowacourse.friendogly.databinding.ItemGroupDogImageBinding
import com.woowacourse.friendogly.presentation.ui.group.list.model.GroupWoof

class GroupWoofAdapter : ListAdapter<GroupWoof, GroupWoofViewHolder>(GroupWoofDiffCallback()) {
    class GroupWoofDiffCallback : DiffUtil.ItemCallback<GroupWoof>() {
        override fun areItemsTheSame(
            oldItem: GroupWoof,
            newItem: GroupWoof,
        ): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }

        override fun areContentsTheSame(
            oldItem: GroupWoof,
            newItem: GroupWoof,
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupWoofViewHolder {
        val binding = ItemGroupDogImageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return GroupWoofViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupWoofViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
