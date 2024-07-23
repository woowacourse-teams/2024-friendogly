package com.woowacourse.friendogly.presentation.ui.group.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.woowacourse.friendogly.databinding.ItemDetailProfileBinding
import com.woowacourse.friendogly.presentation.ui.group.detail.model.GroupDetailProfileUiModel

class DetailProfileAdapter :
    ListAdapter<GroupDetailProfileUiModel, DetailProfileViewHolder>(DetailProfileDiffCallback()) {
    class DetailProfileDiffCallback : DiffUtil.ItemCallback<GroupDetailProfileUiModel>() {
        override fun areItemsTheSame(
            oldItem: GroupDetailProfileUiModel,
            newItem: GroupDetailProfileUiModel,
        ): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }

        override fun areContentsTheSame(
            oldItem: GroupDetailProfileUiModel,
            newItem: GroupDetailProfileUiModel,
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
        return DetailProfileViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DetailProfileViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }
}
