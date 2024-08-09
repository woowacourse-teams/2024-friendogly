package com.happy.friendogly.presentation.ui.club.list.adapter.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.happy.friendogly.databinding.ItemFilterBinding
import com.happy.friendogly.presentation.ui.club.model.clubfilter.ClubFilter

class FilterAdapter : ListAdapter<ClubFilter, FilterViewHolder>(FilterDiffCallback()) {
    class FilterDiffCallback : DiffUtil.ItemCallback<ClubFilter>() {
        override fun areItemsTheSame(
            oldItem: ClubFilter,
            newItem: ClubFilter,
        ): Boolean {
            return oldItem.filterName == newItem.filterName
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
    ): FilterViewHolder {
        val binding = ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FilterViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }
}
