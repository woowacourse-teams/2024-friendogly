package com.happy.friendogly.presentation.ui.woof.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.happy.friendogly.databinding.ItemFootprintInfoPetDetailBinding
import com.happy.friendogly.presentation.ui.woof.WoofActionHandler
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoPetDetailUiModel

class FootprintInfoPetDetailAdapter(private val actionHandler: WoofActionHandler) :
    ListAdapter<FootprintInfoPetDetailUiModel, FootprintInfoPetDetailViewHolder>(diffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FootprintInfoPetDetailViewHolder {
        val binding =
            ItemFootprintInfoPetDetailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return FootprintInfoPetDetailViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FootprintInfoPetDetailViewHolder,
        position: Int,
    ) {
        holder.bind(footprintPetDetail = getItem(position), actionHandler = actionHandler)
    }

    companion object {
        private val diffUtil =
            object : DiffUtil.ItemCallback<FootprintInfoPetDetailUiModel>() {
                override fun areItemsTheSame(
                    oldItem: FootprintInfoPetDetailUiModel,
                    newItem: FootprintInfoPetDetailUiModel,
                ): Boolean {
                    return oldItem.petDetail.name == newItem.petDetail.name
                }

                override fun areContentsTheSame(
                    oldItem: FootprintInfoPetDetailUiModel,
                    newItem: FootprintInfoPetDetailUiModel,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
