package com.happy.friendogly.presentation.ui.petdetail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemDogDetailBinding
import com.happy.friendogly.presentation.ui.mypage.Dog

class PetDetailAdapter : ListAdapter<Dog, PetDetailAdapter.ViewHolder>(PetItemDiffCallback) {
    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDogDetailBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position % currentList.size))
    }

    class ViewHolder(private val binding: ItemDogDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Dog) {
            binding.dog = item
        }
    }

    internal object PetItemDiffCallback : DiffUtil.ItemCallback<Dog>() {
        override fun areItemsTheSame(
            oldItem: Dog,
            newItem: Dog,
        ): Boolean = oldItem.name == newItem.name

        override fun areContentsTheSame(
            oldItem: Dog,
            newItem: Dog,
        ): Boolean = oldItem == newItem
    }
}
