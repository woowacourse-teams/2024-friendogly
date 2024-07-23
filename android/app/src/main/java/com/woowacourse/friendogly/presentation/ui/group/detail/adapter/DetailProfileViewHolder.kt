package com.woowacourse.friendogly.presentation.ui.group.detail.adapter

import androidx.recyclerview.widget.RecyclerView
import com.woowacourse.friendogly.databinding.ItemDetailProfileBinding
import com.woowacourse.friendogly.presentation.ui.group.detail.model.GroupDetailProfileUiModel

class DetailProfileViewHolder(
    private val binding: ItemDetailProfileBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(profile: GroupDetailProfileUiModel) {
        binding.profile = profile
    }
}
