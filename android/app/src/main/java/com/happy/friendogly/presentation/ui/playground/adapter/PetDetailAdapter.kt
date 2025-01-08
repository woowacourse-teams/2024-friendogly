package com.happy.friendogly.presentation.ui.playground.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.happy.friendogly.databinding.ItemPlaygroundMyPetDetailBinding
import com.happy.friendogly.databinding.ItemPlaygroundPetDetailBinding
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundActionHandler
import com.happy.friendogly.presentation.ui.playground.uimodel.PlaygroundPetDetailUiModel

class PetDetailAdapter(private val actionHandler: PlaygroundActionHandler) :
    ListAdapter<PlaygroundPetDetailUiModel, ViewHolder>(diffUtil) {
    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == MY_PET_DETAIL_VIEW_TYPE) {
            val binding =
                ItemPlaygroundMyPetDetailBinding.inflate(
                    inflater,
                    parent,
                    false,
                )
            MyPetDetailViewHolder(binding)
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
        if (holder is MyPetDetailViewHolder) {
            holder.bind(petDetail = getItem(position), actionHandler = actionHandler)
        }

        if (holder is PetDetailViewHolder) {
            holder.bind(petDetail = getItem(position), actionHandler = actionHandler)
        }
    }

    companion object {
        private val diffUtil =
            object : DiffUtil.ItemCallback<PlaygroundPetDetailUiModel>() {
                override fun areItemsTheSame(
                    oldItem: PlaygroundPetDetailUiModel,
                    newItem: PlaygroundPetDetailUiModel,
                ): Boolean {
                    return oldItem.petId == newItem.petId
                }

                override fun areContentsTheSame(
                    oldItem: PlaygroundPetDetailUiModel,
                    newItem: PlaygroundPetDetailUiModel,
                ): Boolean {
                    return oldItem == newItem
                }
            }

        const val MY_PET_DETAIL_VIEW_TYPE = 1
        const val PET_DETAIL_VIEW_TYPE = 2
    }
}
