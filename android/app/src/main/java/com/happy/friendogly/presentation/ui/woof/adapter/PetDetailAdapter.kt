package com.happy.friendogly.presentation.ui.woof.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.happy.friendogly.databinding.ItemPlaygroundFirstPetDetailBinding
import com.happy.friendogly.databinding.ItemPlaygroundPetDetailBinding
import com.happy.friendogly.presentation.ui.woof.action.WoofActionHandler
import com.happy.friendogly.presentation.ui.woof.uimodel.PetDetailInfoUiModel

class PetDetailAdapter(private val actionHandler: WoofActionHandler) :
    ListAdapter<PetDetailInfoUiModel, ViewHolder>(diffUtil) {
    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == FIRST_PET_DETAIL_VIEW_TYPE) {
            val binding =
                ItemPlaygroundFirstPetDetailBinding.inflate(
                    inflater,
                    parent,
                    false,
                )
            FirstPetDetailViewHolder(binding)
        } else {
            val binding =
                ItemPlaygroundPetDetailBinding.inflate(
                    inflater,
                    parent,
                    false,
                )
            PetDetailViewHolder(binding)
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        if (holder is FirstPetDetailViewHolder) {
            holder.bind(petDetailInfo = getItem(position), actionHandler = actionHandler)
        }

        if (holder is PetDetailViewHolder) {
            holder.bind(petDetailInfo = getItem(position), actionHandler = actionHandler)
        }
    }

    companion object {
        private val diffUtil =
            object : DiffUtil.ItemCallback<PetDetailInfoUiModel>() {
                override fun areItemsTheSame(
                    oldItem: PetDetailInfoUiModel,
                    newItem: PetDetailInfoUiModel,
                ): Boolean {
                    return oldItem.memberId == newItem.memberId
                }

                override fun areContentsTheSame(
                    oldItem: PetDetailInfoUiModel,
                    newItem: PetDetailInfoUiModel,
                ): Boolean {
                    return oldItem == newItem
                }
            }

        const val FIRST_PET_DETAIL_VIEW_TYPE = 1
        const val PET_DETAIL_VIEW_TYPE = 2
    }
}
