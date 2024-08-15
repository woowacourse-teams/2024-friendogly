package com.happy.friendogly.presentation.ui.chatlist.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.happy.friendogly.databinding.ItemChatComeOutBinding
import com.happy.friendogly.databinding.ItemChatDateBinding
import com.happy.friendogly.databinding.ItemChatMineBinding
import com.happy.friendogly.databinding.ItemChatOtherBinding
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatNavigationAction
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatUiModel

class ChatAdapter(
    private val onMemberClick: ChatNavigationAction,
) : ListAdapter<ChatUiModel, ChatViewHolder>(ChatDiffCallback) {
    init {
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is ChatUiModel.Date -> ChatType.DATE.value
            is ChatUiModel.ComeOut -> ChatType.COME_OUT.value
            is ChatUiModel.Mine -> ChatType.MINE.value
            is ChatUiModel.Other -> ChatType.OTHER.value
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ChatType.DATE.value ->
                DateViewHolder(
                    ItemChatDateBinding.inflate(
                        inflater,
                        parent,
                        false,
                    ),
                )

            ChatType.COME_OUT.value ->
                ComeOutViewHolder(
                    ItemChatComeOutBinding.inflate(
                        inflater,
                        parent,
                        false,
                    ),
                )

            ChatType.MINE.value ->
                MineViewHolder(
                    ItemChatMineBinding.inflate(
                        inflater,
                        parent,
                        false,
                    ),
                )

            ChatType.OTHER.value ->
                OtherViewHolder(
                    ItemChatOtherBinding.inflate(
                        inflater,
                        parent,
                        false,
                    ),
                    onMemberClick,
                )

            else -> error("$viewType 잘못된 viewType이 들어왔습니다")
        }
    }

    override fun onBindViewHolder(
        holder: ChatViewHolder,
        position: Int,
    ) {
        when (holder) {
            is ComeOutViewHolder -> holder.bind(getItem(position) as ChatUiModel.ComeOut)
            is DateViewHolder -> holder.bind(getItem(position) as ChatUiModel.Date)
            is MineViewHolder -> holder.bind(getItem(position) as ChatUiModel.Mine)
            is OtherViewHolder -> holder.bind(getItem(position) as ChatUiModel.Other)
        }
    }

    companion object {
        private object ChatDiffCallback : DiffUtil.ItemCallback<ChatUiModel>() {
            override fun areItemsTheSame(
                oldItem: ChatUiModel,
                newItem: ChatUiModel,
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ChatUiModel,
                newItem: ChatUiModel,
            ): Boolean = oldItem == newItem
        }
    }
}
