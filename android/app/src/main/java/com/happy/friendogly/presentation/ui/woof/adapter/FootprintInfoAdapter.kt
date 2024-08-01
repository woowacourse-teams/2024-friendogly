package com.happy.friendogly.presentation.ui.woof.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.happy.friendogly.databinding.ItemPetDetailBinding
import com.happy.friendogly.presentation.ui.woof.model.PetDetail

class FootprintInfoAdapter :
    ListAdapter<PetDetail, PetDetailViewHolder>(diffUtil) {
    private lateinit var memberName: String

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PetDetailViewHolder {
        val binding =
            ItemPetDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetDetailViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PetDetailViewHolder,
        position: Int,
    ) {
        holder.bind(memberName = memberName, petDetail = getItem(position))
    }

    fun setMemberName(memberName: String) {
        this.memberName = memberName
    }

    companion object {
        private val diffUtil =
            object : DiffUtil.ItemCallback<PetDetail>() {
                override fun areItemsTheSame(
                    oldItem: PetDetail,
                    newItem: PetDetail,
                ): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(
                    oldItem: PetDetail,
                    newItem: PetDetail,
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
