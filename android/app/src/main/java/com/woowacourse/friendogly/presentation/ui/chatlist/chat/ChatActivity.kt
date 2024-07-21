package com.woowacourse.friendogly.presentation.ui.chatlist.chat

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.ActivityChatBinding
import com.woowacourse.friendogly.presentation.base.BaseActivity
import com.woowacourse.friendogly.presentation.ui.chatlist.chat.adapter.ChatAdapter

class ChatActivity : BaseActivity<ActivityChatBinding>(R.layout.activity_chat) {
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var adapter: ChatAdapter

    override fun initCreateView() {
        initAdapter()

        getChatList()

        clickBackBtn()

    }

    private fun clickBackBtn() {
        binding.toolbarChat.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initAdapter() {
        adapter = ChatAdapter()
        binding.rcvChatDetail.adapter = adapter
    }

    private fun getChatList() {
        val chatId: Long = intent.getLongExtra(EXTRA_CHAT_ID, INVALID_ID)
        viewModel.getChats(chatId)
        viewModel.chats.observe(this) {
            adapter.submitList(it)
        }
    }

    companion object {
        private const val INVALID_ID = -1L

        private const val EXTRA_CHAT_ID = "chatId"

        fun getIntent(
            context: Context,
            chatId: Long,
        ): Intent {
            return Intent(context, ChatActivity::class.java).apply {
                putExtra(EXTRA_CHAT_ID, chatId)
            }
        }
    }
}
