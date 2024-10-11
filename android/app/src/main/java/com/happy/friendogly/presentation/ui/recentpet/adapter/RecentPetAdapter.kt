package com.happy.friendogly.presentation.ui.recentpet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemRecentPetBinding
import com.happy.friendogly.databinding.ItemRecentPetDateBinding
import com.happy.friendogly.presentation.ui.recentpet.RecentPetActionHandler
import com.happy.friendogly.presentation.ui.recentpet.RecentPetDateView
import com.happy.friendogly.presentation.ui.recentpet.RecentPetView
import com.happy.friendogly.presentation.ui.recentpet.RecentPetViewType

class RecentPetAdapter(
    private val actionHandler: RecentPetActionHandler,
) : ListAdapter<RecentPetViewType, RecentPetAdapter.RecentPetViewHolder>(RecentPetItemDiffCallback) {
    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is RecentPetView -> RECENT_PET_VIEW_TYPE
            is RecentPetDateView -> DATE_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecentPetViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            RECENT_PET_VIEW_TYPE -> {
                val binding = ItemRecentPetBinding.inflate(inflater, parent, false)
                binding.actionHandler = actionHandler
                RecentPetViewHolder.PetViewHolder(binding)
            }

            else -> {
                val binding = ItemRecentPetDateBinding.inflate(inflater, parent, false)
                RecentPetViewHolder.DateViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecentPetViewHolder,
        position: Int,
    ) {
        when (holder) {
            is RecentPetViewHolder.PetViewHolder -> holder.bind(getItem(position) as RecentPetView)
            is RecentPetViewHolder.DateViewHolder -> holder.bind(getItem(position) as RecentPetDateView)
        }
    }

    sealed class RecentPetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        class PetViewHolder(
            private val binding: ItemRecentPetBinding,
        ) : RecentPetViewHolder(binding.root) {
            fun bind(item: RecentPetView) {
                binding.recentPetView = item
            }
        }

        class DateViewHolder(
            private val binding: ItemRecentPetDateBinding,
        ) : RecentPetViewHolder(binding.root) {
            fun bind(item: RecentPetDateView) {
                binding.recentPetDateView = item
            }
        }
    }

    companion object RecentPetItemDiffCallback : DiffUtil.ItemCallback<RecentPetViewType>() {
        const val RECENT_PET_VIEW_TYPE = 0
        const val DATE_VIEW_TYPE = 1

        override fun areItemsTheSame(
            oldItem: RecentPetViewType,
            newItem: RecentPetViewType,
        ): Boolean = oldItem.index == newItem.index

        override fun areContentsTheSame(
            oldItem: RecentPetViewType,
            newItem: RecentPetViewType,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
