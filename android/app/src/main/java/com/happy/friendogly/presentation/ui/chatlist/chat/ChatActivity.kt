package com.happy.friendogly.presentation.ui.chatlist.chat

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.ActivityChatBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.ui.chatlist.chat.adapter.ChatAdapter
import com.happy.friendogly.presentation.ui.chatlist.chatinfo.ChatInfoSideSheet

class ChatActivity : BaseActivity<ActivityChatBinding>(R.layout.activity_chat) {
    private val viewModel: ChatViewModel by viewModels {
        ChatViewModel.factory(
            AppModule.getInstance().webSocketRepository,
        )
    }
    private lateinit var adapter: ChatAdapter

    override fun initCreateView() {
        binding.vm = viewModel

        initAdapter()
        getChatList()
        clickBackBtn()
        clickChatInfo()
        val chatId: Long = intent.getLongExtra(EXTRA_CHAT_ID, INVALID_ID)

        binding.ibChatSendMessage.setOnClickListener {
            viewModel.sendMessage(chatId, binding.edtChatSendMessage.text.toString())
            binding.edtChatSendMessage.setText("")
        }
        viewModel.subscribeMessage(chatId)
    }

    private fun clickChatInfo() {
        binding.ibChatSideMenu.setOnClickListener {
            ChatInfoSideSheet().show(supportFragmentManager, "")
        }
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
        viewModel.chats.observe(this) {
            adapter.submitList(it)
        }
    }

    companion object {
        private const val INVALID_ID = -1L

        private const val EXTRA_CHAT_ID = "chatId"
        private const val EXTRA_MEMBER_ID = "memberId"

        fun getIntent(
            context: Context,
            chatId: Long,
            memberId: Long,
        ): Intent {
            return Intent(context, ChatActivity::class.java).apply {
                putExtra(EXTRA_CHAT_ID, chatId)
                putExtra(EXTRA_MEMBER_ID, memberId)
            }
        }
    }
}
