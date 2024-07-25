package com.happy.friendogly.presentation.ui.group.select.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.happy.friendogly.databinding.ItemDogSelectProfileBinding
import com.happy.friendogly.presentation.ui.group.select.DogSelectActionHandler
import com.happy.friendogly.presentation.ui.group.select.DogSelectUiModel

class DogSelectAdapter(
    private val actionHandler: DogSelectActionHandler,
) : ListAdapter<DogSelectUiModel, DogSelectViewHolder>(DogSelectDiffCallback()) {
    class DogSelectDiffCallback : DiffUtil.ItemCallback<DogSelectUiModel>() {
        override fun areItemsTheSame(
            oldItem: DogSelectUiModel,
            newItem: DogSelectUiModel,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DogSelectUiModel,
            newItem: DogSelectUiModel,
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DogSelectViewHolder {
        val view = ItemDogSelectProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DogSelectViewHolder(view, actionHandler)
    }

    override fun onBindViewHolder(
        holder: DogSelectViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        holder.bind(item)
    }
}
