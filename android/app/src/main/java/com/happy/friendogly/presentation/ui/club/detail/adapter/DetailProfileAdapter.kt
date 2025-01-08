package com.happy.friendogly.presentation.ui.club.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.happy.friendogly.databinding.ItemDetailProfileBinding
import com.happy.friendogly.presentation.ui.club.detail.ClubDetailNavigation
import com.happy.friendogly.presentation.ui.club.detail.model.ClubDetailProfileUiModel

class DetailProfileAdapter(
    private val navigation: ClubDetailNavigation,
) : ListAdapter<ClubDetailProfileUiModel, DetailProfileViewHolder>(DetailProfileDiffCallback()) {
    class DetailProfileDiffCallback : DiffUtil.ItemCallback<ClubDetailProfileUiModel>() {
        override fun areItemsTheSame(
            oldItem: ClubDetailProfileUiModel,
            newItem: ClubDetailProfileUiModel,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ClubDetailProfileUiModel,
            newItem: ClubDetailProfileUiModel,
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DetailProfileViewHolder {
        val binding =
            ItemDetailProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DetailProfileViewHolder(binding, navigation)
    }

    override fun onBindViewHolder(
        holder: DetailProfileViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }
}
