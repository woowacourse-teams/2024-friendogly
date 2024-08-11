package com.happy.friendogly.presentation.ui.chatlist.chatinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.happy.friendogly.databinding.ItemChatJoinPeopleBinding
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatNavigationAction

class JoinPeopleAdapter(
    private val onMemberClick: ChatNavigationAction,
) :
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
        return JoinPeopleViewHolder(binding, onMemberClick)
    }

    override fun onBindViewHolder(
        holder: JoinPeopleViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    class JoinPeopleViewHolder(
        private val binding: ItemChatJoinPeopleBinding,
        private val onClick: ChatNavigationAction,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: JoinPeople) {
            if (item.profileUrl.isNotBlank()) {
                Glide.with(itemView.context).load(item.profileUrl)
                    .into(binding.ivChatJoinPeople)
            }
            binding.tvChatJoinPeopleNickname.text = item.nickName
            binding.tvChatJoinPeopleIsMe.isVisible = item.isMe
            binding.ivChatJoinPeopleLeader.isVisible = item.isLeader
            if (!item.isMe) {
                binding.root.setOnClickListener {
                    onClick.navigateToMemberProfile(item.memberId)
                }
            }
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
