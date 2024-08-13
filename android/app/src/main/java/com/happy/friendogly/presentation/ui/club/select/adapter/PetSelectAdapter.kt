package com.happy.friendogly.presentation.ui.club.select.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.happy.friendogly.databinding.ItemClubSelectProfileBinding
import com.happy.friendogly.presentation.ui.club.select.PetSelectActionHandler
import com.happy.friendogly.presentation.ui.club.select.PetSelectUiModel

class PetSelectAdapter(
    private val actionHandler: PetSelectActionHandler,
) : ListAdapter<PetSelectUiModel, PetSelectViewHolder>(DogSelectDiffCallback()) {
    class DogSelectDiffCallback : DiffUtil.ItemCallback<PetSelectUiModel>() {
        override fun areItemsTheSame(
            oldItem: PetSelectUiModel,
            newItem: PetSelectUiModel,
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: PetSelectUiModel,
            newItem: PetSelectUiModel,
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PetSelectViewHolder {
        val view =
            ItemClubSelectProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetSelectViewHolder(view, actionHandler)
    }

    override fun onBindViewHolder(
        holder: PetSelectViewHolder,
        position: Int,
    ) {
        val item = getItem(position)
        holder.bind(item)
    }
}
