package com.woowacourse.friendogly.presentation.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.woowacourse.friendogly.databinding.ItemPetBinding
import com.woowacourse.friendogly.domain.model.Pet
import com.woowacourse.friendogly.presentation.ui.mypage.MyPageActionHandler

class PetProfileAdapter(
    private val actionHandler: MyPageActionHandler,
) : ListAdapter<Pet, PetProfileAdapter.ViewHolder>(PetItemDiffCallback) {
    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPetBinding.inflate(inflater, parent, false)
        binding.actionHandler = actionHandler
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemPetBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pet) {
            binding.pet = item
        }
    }

    companion object PetItemDiffCallback : DiffUtil.ItemCallback<Pet>() {
        override fun areItemsTheSame(
            oldItem: Pet,
            newItem: Pet,
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Pet,
            newItem: Pet,
        ): Boolean = oldItem == newItem
    }
}
