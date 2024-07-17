package com.woowacourse.friendogly.presentation.ui.group.select.adapter

import androidx.recyclerview.widget.RecyclerView
import com.woowacourse.friendogly.databinding.ItemDogSelectProfileBinding
import com.woowacourse.friendogly.presentation.ui.group.select.DogSelectUiModel

class DogSelectViewHolder(
    private val binding: ItemDogSelectProfileBinding,
): RecyclerView.ViewHolder(binding.root) {
    fun bind(dogSelectUiModel: DogSelectUiModel){
        binding.dogUiModel = dogSelectUiModel
    }
}
