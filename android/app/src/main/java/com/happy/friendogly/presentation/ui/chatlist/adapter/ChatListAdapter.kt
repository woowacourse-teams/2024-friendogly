package com.happy.friendogly.presentation.ui.chatlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.happy.friendogly.databinding.ItemChatListBinding
import com.happy.friendogly.presentation.ui.chatlist.ChatListNavigationAction
import com.happy.friendogly.presentation.ui.chatlist.uimodel.ChatListUiModel

class ChatListAdapter(private val chatListNavigationAction: ChatListNavigationAction) :
    ListAdapter<ChatListUiModel, ChatListAdapter.ChatListViewHolder>(ChatListDiffCallback) {
    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).chatRoomId
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ChatListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChatListBinding.inflate(inflater, parent, false)
        return ChatListViewHolder(binding, chatListNavigationAction)
    }

    override fun onBindViewHolder(
        holder: ChatListViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    class ChatListViewHolder(
        private val binding: ItemChatListBinding,
        private val chatListNavigationAction: ChatListNavigationAction,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChatListUiModel) {
            if (item.imageUrl?.isNotBlank() == true) {
                Glide.with(itemView.context).load(item.imageUrl).transform(CenterCrop())
                    .into(binding.ivChatGroup)
            }
            binding.tvChatTitle.text = item.title
            binding.tvChatBody.text = item.body
            binding.tvChatMemberCount.text = item.numberOfPeople.toString()
            binding.dateTime = item.dateTime
            binding.unreadMessageCount = item.unreadMessageCount
            binding.root.setOnClickListener {
                chatListNavigationAction.navigateToChat(item.chatRoomId)
            }
        }
    }

    companion object {
        private object ChatListDiffCallback : DiffUtil.ItemCallback<ChatListUiModel>() {
            override fun areItemsTheSame(
                oldItem: ChatListUiModel,
                newItem: ChatListUiModel,
            ): Boolean = oldItem.unreadMessageCount == newItem.unreadMessageCount

            override fun areContentsTheSame(
                oldItem: ChatListUiModel,
                newItem: ChatListUiModel,
            ): Boolean = oldItem == newItem
        }
    }
}
