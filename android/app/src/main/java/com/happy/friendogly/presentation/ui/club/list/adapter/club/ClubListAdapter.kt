package com.happy.friendogly.presentation.ui.club.list.adapter.club

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.happy.friendogly.databinding.ItemClubBinding
import com.happy.friendogly.presentation.ui.club.common.ClubItemActionHandler
import com.happy.friendogly.presentation.ui.club.common.model.ClubItemUiModel

class ClubListAdapter(
    private val actionHandler: ClubItemActionHandler,
) : PagingDataAdapter<ClubItemUiModel, ClubViewHolder>(diffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ClubViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ClubViewHolder(ItemClubBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: ClubViewHolder,
        position: Int,
    ) {
        val item = getItem(position) ?: return
        holder.bind(item, actionHandler)
    }

    companion object {
        private val diffCallback =
            object : DiffUtil.ItemCallback<ClubItemUiModel>() {
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
    }
}
