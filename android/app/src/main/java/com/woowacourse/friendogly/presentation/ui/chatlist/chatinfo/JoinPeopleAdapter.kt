package com.woowacourse.friendogly.presentation.ui.chatlist.chatinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.woowacourse.friendogly.databinding.ItemChatJoinPeopleBinding

class JoinPeopleAdapter :
    ListAdapter<JoinPeople, JoinPeopleAdapter.JoinPeopleViewHolder>(ChatInfoDiffCallback) {
    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): JoinPeopleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatJoinPeopleBinding.inflate(inflater, parent, false)
        return JoinPeopleViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: JoinPeopleViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    class JoinPeopleViewHolder(
        private val binding: ItemChatJoinPeopleBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: JoinPeople) {
            Glide.with(itemView.context).load(item.profileUrl)
                .into(binding.ivChatJoinPeople)
            binding.tvChatJoinPeopleNickname.text = item.nickName
            binding.tvChatJoinPeopleIsMe.isVisible = item.isMe
            binding.ivChatJoinPeopleLeader.isVisible = item.isLeader

        }
    }

    companion object {
        private object ChatInfoDiffCallback : DiffUtil.ItemCallback<JoinPeople>() {
            override fun areItemsTheSame(
                oldItem: JoinPeople,
                newItem: JoinPeople,
            ): Boolean = oldItem.nickName == newItem.nickName

            override fun areContentsTheSame(
                oldItem: JoinPeople,
                newItem: JoinPeople,
            ): Boolean = oldItem == newItem
        }
    }
}
