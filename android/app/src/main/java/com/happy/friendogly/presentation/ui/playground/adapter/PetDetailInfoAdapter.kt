package com.happy.friendogly.presentation.ui.playground.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.happy.friendogly.databinding.ItemPlaygroundPetDetailBinding
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundActionHandler
import com.happy.friendogly.presentation.ui.playground.uimodel.PetDetailInfoUiModel

class PetDetailInfoAdapter(private val actionHandler: PlaygroundActionHandler) :
    ListAdapter<PetDetailInfoUiModel, PetDetailInfoViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PetDetailInfoViewHolder {
        val binding =
            ItemPlaygroundPetDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return PetDetailInfoViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PetDetailInfoViewHolder,
        position: Int,
    ) {
        holder.bind(petDetailInfo = getItem(position), actionHandler = actionHandler)
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
    }
}
