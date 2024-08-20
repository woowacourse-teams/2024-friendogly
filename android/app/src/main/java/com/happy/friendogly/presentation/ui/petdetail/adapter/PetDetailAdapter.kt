package com.happy.friendogly.presentation.ui.petdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemPetDetailBinding
import com.happy.friendogly.presentation.ui.petdetail.PetDetail

class PetDetailAdapter :
    ListAdapter<PetDetail, PetDetailAdapter.ViewHolder>(PetDetailItemDiffCallback) {
    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPetDetailBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemPetDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PetDetail) {
            binding.petDetail = item
        }
    }

    internal object PetDetailItemDiffCallback : DiffUtil.ItemCallback<PetDetail>() {
        override fun areItemsTheSame(
            oldItem: PetDetail,
            newItem: PetDetail,
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: PetDetail,
            newItem: PetDetail,
        ): Boolean = oldItem == newItem
    }
}
