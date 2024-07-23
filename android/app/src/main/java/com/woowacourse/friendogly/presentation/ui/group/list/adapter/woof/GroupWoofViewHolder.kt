package com.woowacourse.friendogly.presentation.ui.group.list.adapter.woof

import androidx.recyclerview.widget.RecyclerView
import com.woowacourse.friendogly.databinding.ItemGroupDogImageBinding
import com.woowacourse.friendogly.presentation.ui.group.list.model.GroupWoof

class GroupWoofViewHolder(
    private val binding: ItemGroupDogImageBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(woof: GroupWoof) {
        binding.woof = woof
    }
}
