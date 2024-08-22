package com.happy.friendogly.presentation.ui.otherprofile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemOtherPetProfileBinding
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.presentation.ui.otherprofile.OtherProfileActionHandler

class OtherPetProfileAdapter(
    private val actionHandler: OtherProfileActionHandler,
) : ListAdapter<Pet, OtherPetProfileAdapter.OtherPetProfileViewHolder>(OtherPetItemDiffCallback) {
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): OtherPetProfileViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = ItemOtherPetProfileBinding.inflate(inflater, parent, false)
        binding.actionHandler = actionHandler
        return OtherPetProfileViewHolder(binding)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(
        holder: OtherPetProfileViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    class OtherPetProfileViewHolder(
        private val binding: ItemOtherPetProfileBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pet) {
            binding.pet = item
        }
    }

    companion object OtherPetItemDiffCallback : DiffUtil.ItemCallback<Pet>() {
        override fun areItemsTheSame(
            oldItem: Pet,
            newItem: Pet,
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Pet,
            newItem: Pet,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
