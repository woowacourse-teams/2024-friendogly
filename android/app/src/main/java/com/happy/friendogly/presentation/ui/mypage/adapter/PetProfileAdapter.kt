package com.happy.friendogly.presentation.ui.mypage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.happy.friendogly.databinding.ItemPetAddBinding
import com.happy.friendogly.databinding.ItemPetProfileBinding
import com.happy.friendogly.presentation.ui.mypage.MyPageActionHandler
import com.happy.friendogly.presentation.ui.mypage.PetAddView
import com.happy.friendogly.presentation.ui.mypage.PetView
import com.happy.friendogly.presentation.ui.mypage.PetViewType

class PetProfileAdapter(
    private val actionHandler: MyPageActionHandler,
) : ListAdapter<PetViewType, PetProfileAdapter.PetViewHolder>(PetItemDiffCallback) {
    init {
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        return if (currentList[position] is PetAddView) PET_ADD_VIEW_TYPE else PET_PROFILE_VIEW_TYPE
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PetViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            PET_PROFILE_VIEW_TYPE -> {
                val binding = ItemPetProfileBinding.inflate(inflater, parent, false)
                binding.actionHandler = actionHandler
                PetViewHolder.PetProfileViewHolder(binding)
            }

            else -> {
                val binding = ItemPetAddBinding.inflate(inflater, parent, false)
                binding.actionHandler = actionHandler
                PetViewHolder.PetAddViewHolder(binding)
            }
        }
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(
        holder: PetViewHolder,
        position: Int,
    ) {
        when (holder) {
            is PetViewHolder.PetProfileViewHolder -> holder.bind(getItem(position) as PetView)
            is PetViewHolder.PetAddViewHolder -> holder.bind(getItem(position) as PetAddView)
        }
    }

    sealed class PetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        class PetProfileViewHolder(
            private val binding: ItemPetProfileBinding,
        ) : PetViewHolder(binding.root) {
            fun bind(item: PetView) {
                binding.petView = item
            }
        }

        class PetAddViewHolder(
            private val binding: ItemPetAddBinding,
        ) : PetViewHolder(binding.root) {
            fun bind(item: PetAddView) {
                binding.petAddView = item
            }
        }
    }

    companion object PetItemDiffCallback : DiffUtil.ItemCallback<PetViewType>() {
        const val PET_PROFILE_VIEW_TYPE = 0
        const val PET_ADD_VIEW_TYPE = 1

        override fun areItemsTheSame(
            oldItem: PetViewType,
            newItem: PetViewType,
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: PetViewType,
            newItem: PetViewType,
        ): Boolean {
            return oldItem == newItem
        }
    }
}
