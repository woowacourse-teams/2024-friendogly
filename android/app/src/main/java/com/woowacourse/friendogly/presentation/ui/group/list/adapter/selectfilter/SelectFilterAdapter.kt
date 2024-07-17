package com.woowacourse.friendogly.presentation.ui.group.list.adapter.selectfilter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.woowacourse.friendogly.databinding.ItemFilterBinding
import com.woowacourse.friendogly.databinding.ItemSelectFilterBinding
import com.woowacourse.friendogly.presentation.ui.group.list.GroupListActionHandler
import com.woowacourse.friendogly.presentation.ui.group.list.model.groupfilter.GroupFilter

class SelectFilterAdapter(
    private val actionHandler: GroupListActionHandler,
) : ListAdapter<GroupFilter, SelectFilterViewHolder>(FilterDiffCallback()) {
    class FilterDiffCallback : DiffUtil.ItemCallback<GroupFilter>() {
        override fun areItemsTheSame(oldItem: GroupFilter, newItem: GroupFilter): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: GroupFilter, newItem: GroupFilter): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectFilterViewHolder {
        val binding = ItemSelectFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectFilterViewHolder, position: Int) {
        holder.bind(getItem(position),actionHandler)
    }
}
