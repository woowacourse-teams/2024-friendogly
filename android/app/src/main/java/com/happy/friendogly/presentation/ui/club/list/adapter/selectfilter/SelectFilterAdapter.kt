package com.happy.friendogly.presentation.ui.club.list.adapter.selectfilter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.happy.friendogly.databinding.ItemSelectedFilterMenuBinding
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.list.ClubListActionHandler

class SelectFilterAdapter(
    private val actionHandler: ClubListActionHandler,
) : ListAdapter<ClubFilter, SelectFilterViewHolder>(FilterDiffCallback()) {
    class FilterDiffCallback : DiffUtil.ItemCallback<ClubFilter>() {
        override fun areItemsTheSame(
            oldItem: ClubFilter,
            newItem: ClubFilter,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ClubFilter,
            newItem: ClubFilter,
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SelectFilterViewHolder {
        val binding =
            ItemSelectedFilterMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return SelectFilterViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SelectFilterViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position), actionHandler)
    }
}
