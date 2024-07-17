package com.woowacourse.friendogly.presentation.ui.group.select.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.woowacourse.friendogly.databinding.ItemDogSelectProfileBinding
import com.woowacourse.friendogly.presentation.ui.group.select.DogSelectUiModel

class DogSelectAdapter(

): ListAdapter<DogSelectUiModel,DogSelectViewHolder>(DogSelectDiffCallback()) {
    class DogSelectDiffCallback: DiffUtil.ItemCallback<DogSelectUiModel>() {
        override fun areItemsTheSame(
            oldItem: DogSelectUiModel,
            newItem: DogSelectUiModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: DogSelectUiModel,
            newItem: DogSelectUiModel,
        ): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogSelectViewHolder {
        val view = ItemDogSelectProfileBinding.inflate(LayoutInflater.from(parent.context))
        return DogSelectViewHolder(view)
    }

    override fun onBindViewHolder(holder: DogSelectViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}
