package com.woowacourse.friendogly.presentation.ui.group.list.adapter.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.woowacourse.friendogly.databinding.ItemFilterBinding
import com.woowacourse.friendogly.presentation.ui.group.list.model.groupfilter.GroupFilter

class FilterAdapter: ListAdapter<GroupFilter,FilterViewHolder>(FilterDiffCallback()) {
    class FilterDiffCallback: DiffUtil.ItemCallback<GroupFilter>() {
        override fun areItemsTheSame(oldItem: GroupFilter, newItem: GroupFilter): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GroupFilter, newItem: GroupFilter): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
