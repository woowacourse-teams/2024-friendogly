package com.happy.friendogly.presentation.ui.club.my.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.happy.friendogly.databinding.ItemClubBinding
import com.happy.friendogly.presentation.ui.club.common.ClubItemActionHandler
import com.happy.friendogly.presentation.ui.club.common.model.ClubItemUiModel
import com.happy.friendogly.presentation.ui.club.list.adapter.club.ClubViewHolder

class MyClubListAdapter(
    private val actionHandler: ClubItemActionHandler,
) : ListAdapter<ClubItemUiModel, ClubViewHolder>(ClubDiffCallback()) {
    class ClubDiffCallback : DiffUtil.ItemCallback<ClubItemUiModel>() {
        override fun areItemsTheSame(
            oldItem: ClubItemUiModel,
            newItem: ClubItemUiModel,
        ): Boolean {
            return oldItem.clubId == newItem.clubId
        }

        override fun areContentsTheSame(
            oldItem: ClubItemUiModel,
            newItem: ClubItemUiModel,
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
