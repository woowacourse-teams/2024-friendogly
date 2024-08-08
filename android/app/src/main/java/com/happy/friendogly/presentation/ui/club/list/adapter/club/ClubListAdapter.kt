package com.happy.friendogly.presentation.ui.club.list.adapter.club

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.happy.friendogly.databinding.ItemClubBinding
import com.happy.friendogly.presentation.ui.club.list.ClubListActionHandler
import com.happy.friendogly.presentation.ui.club.list.ClubListUiModel

class ClubListAdapter(
    private val actionHandler: ClubListActionHandler,
) : ListAdapter<ClubListUiModel, ClubViewHolder>(ClubDiffCallback()) {
    class ClubDiffCallback : DiffUtil.ItemCallback<ClubListUiModel>() {
        override fun areItemsTheSame(
            oldItem: ClubListUiModel,
            newItem: ClubListUiModel,
        ): Boolean {
            return oldItem.clubId == newItem.clubId
        }

        override fun areContentsTheSame(
            oldItem: ClubListUiModel,
            newItem: ClubListUiModel,
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ClubViewHolder {
        val binding = ItemClubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ClubViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ClubViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position), actionHandler)
    }
}
