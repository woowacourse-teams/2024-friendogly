package com.happy.friendogly.presentation.ui.playground.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.happy.friendogly.databinding.ItemPlaygroundPetSummaryBinding

class PetSummaryAdapter :
    ListAdapter<String, PetSummaryViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PetSummaryViewHolder {
        val binding =
            ItemPlaygroundPetSummaryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return PetSummaryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PetSummaryViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    override fun submitList(list: List<String?>?) {
        val petList = list?.toMutableList() ?: mutableListOf()
        while (petList.size < DEFAULT_PET_SIZE) {
            petList.add("")
        }

        super.submitList(petList)
    }

    companion object {
        private val diffUtil =
            object : DiffUtil.ItemCallback<String>() {
                override fun areItemsTheSame(
                    oldItem: String,
                    newItem: String,
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: String,
                    newItem: String,
                ): Boolean {
                    return oldItem == newItem
                }
            }

        private const val DEFAULT_PET_SIZE = 5
    }
}
