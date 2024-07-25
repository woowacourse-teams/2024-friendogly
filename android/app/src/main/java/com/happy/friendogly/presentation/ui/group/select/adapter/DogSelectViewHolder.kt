package com.happy.friendogly.presentation.ui.group.select.adapter

import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemDogSelectProfileBinding
import com.happy.friendogly.presentation.ui.group.select.DogSelectActionHandler
import com.happy.friendogly.presentation.ui.group.select.DogSelectUiModel

class DogSelectViewHolder(
    private val binding: ItemDogSelectProfileBinding,
    private val actionHandler: DogSelectActionHandler,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(dogSelectUiModel: DogSelectUiModel) {
        binding.dogUiModel = dogSelectUiModel
        binding.dogSelectActionHandler = actionHandler
    }
}
