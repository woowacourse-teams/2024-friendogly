package com.woowacourse.friendogly.presentation.ui.chatlist.chat.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.ItemChatComeOutBinding
import com.woowacourse.friendogly.databinding.ItemChatDateBinding
import com.woowacourse.friendogly.databinding.ItemChatMineBinding
import com.woowacourse.friendogly.databinding.ItemChatOtherBinding
import com.woowacourse.friendogly.presentation.ui.chatlist.chat.ChatUiModel
import java.time.format.DateTimeFormatter

sealed class ChatViewHolder(binding: View) : RecyclerView.ViewHolder(binding)

class DateViewHolder(val binding: ItemChatDateBinding) : ChatViewHolder(binding.root) {
    fun bind(item: ChatUiModel.Date) {
        val dateFormatter =
            DateTimeFormatter.ofPattern(itemView.context.getString(R.string.chat_date))
        binding.tvChatDate.text = item.date.format(dateFormatter)
    }
}

class ComeOutViewHolder(val binding: ItemChatComeOutBinding) : ChatViewHolder(binding.root) {
    fun bind(item: ChatUiModel.ComeOut) {
        binding.tvChatComeMessage.text =
            itemView.context.getString(getComeOutString(item))
                .format(item.nickName)
    }

    private fun getComeOutString(item: ChatUiModel.ComeOut) = if (item.isCome) R.string.chat_come_message else R.string.chat_out_message
}

class MineViewHolder(val binding: ItemChatMineBinding) : ChatViewHolder(binding.root) {
    fun bind(item: ChatUiModel.Mine) {
        binding.tvChatMineMessage.text = item.message
        val timeFormatter =
            DateTimeFormatter.ofPattern(itemView.context.getString(R.string.chat_time))
        binding.tvChatMineTime.text = item.time.format(timeFormatter)
    }
}

class OtherViewHolder(val binding: ItemChatOtherBinding) : ChatViewHolder(binding.root) {
    fun bind(item: ChatUiModel.Other) {
        binding.tvChatOtherMessage.text = item.message
        val timeFormatter =
            DateTimeFormatter.ofPattern(itemView.context.getString(R.string.chat_time))
        binding.tvChatOtherTime.text = item.time.format(timeFormatter)
        binding.tvChatUserNickname.text = item.nickName
        binding.profileUrl = item.profileUrl
    }
}
