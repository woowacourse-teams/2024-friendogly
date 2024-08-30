package com.happy.friendogly.presentation.ui.chatlist.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessagingService
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.ActivityChatBinding
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.ui.chatlist.chat.adapter.ChatAdapter
import com.happy.friendogly.presentation.ui.chatlist.chatinfo.ChatInfoSideSheet
import com.happy.friendogly.presentation.ui.club.detail.ClubDetailActivity
import com.happy.friendogly.presentation.ui.otherprofile.OtherProfileActivity
import com.happy.friendogly.presentation.utils.hideKeyboard
import com.happy.friendogly.presentation.utils.logChatAlarmClicked
import com.happy.friendogly.presentation.utils.logChatSendMessageClicked
import com.happy.friendogly.presentation.utils.logPermissionLocationDenied
import com.happy.friendogly.presentation.utils.logWalkHelpClicked
import kotlinx.coroutines.launch
import android.util.Log

class ChatActivity :
    BaseActivity<ActivityChatBinding>(R.layout.activity_chat),
    ChatNavigationAction {
    private val viewModel: ChatViewModel by viewModels {
        ChatViewModel.factory(
            AppModule.getInstance().getChatRoomClubUseCase,
            AppModule.getInstance().getChatMessagesUseCase,
            AppModule.getInstance().connectWebsocketUseCase,
            AppModule.getInstance().disconnectWebsocketUseCase,
            AppModule.getInstance().subScribeMessageUseCase,
            AppModule.getInstance().publishSendUseCase,
        )
    }
    private lateinit var adapter: ChatAdapter

    override fun initCreateView() {
        binding.vm = viewModel
        lifecycle.addObserver(ChatLifecycleObserver.getInstance())
        initAdapter()
        getChatList()
        clickBackBtn()
        val chatId: Long = intent.getLongExtra(EXTRA_CHAT_ID, INVALID_ID)

        clickChatInfo(chatId)

        clickSendMessage(chatId)
        viewModel.subscribeMessage(chatId)
        hideMessageKeyBoard()
    }

    private fun clickSendMessage(chatId: Long) {
        binding.ibChatSendMessage.setOnClickListener {
            viewModel.sendMessage(chatId, binding.edtChatSendMessage.text.toString())
            binding.edtChatSendMessage.setText("")
            AppModule.getInstance().analyticsHelper.logChatSendMessageClicked()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun hideMessageKeyBoard() {
        binding.rcvChatDetail.setOnTouchListener { _, _ ->
            hideKeyboard()
            binding.edtChatSendMessage.clearFocus()
            false
        }
    }

    private fun clickChatInfo(chatRoomId: Long) {
        binding.ibChatSideMenu.setOnClickListener {
            val chatInfoSideSheet = ChatInfoSideSheet()
            chatInfoSideSheet.arguments =
                ChatInfoSideSheet.getBundle(
                    chatRoomId = chatRoomId,
                )
            chatInfoSideSheet.show(supportFragmentManager, "")
        }
    }

    private fun clickBackBtn() {
        binding.toolbarChat.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initAdapter() {
        adapter = ChatAdapter(this)
        binding.rcvChatDetail.adapter = adapter
    }

    private fun getChatList() {
        lifecycleScope.launch {
            viewModel.chats.collect {
                adapter.submitList(it)
                binding.rcvChatDetail.post {
                    binding.rcvChatDetail.smoothScrollToPosition(it.size)
                }
            }
        }
    }

    override fun navigateToMemberProfile(memberId: Long) {
        startActivity(OtherProfileActivity.getIntent(this, memberId))
    }

    override fun navigateToClub(clubId: Long) {
        startActivity(
            ClubDetailActivity.getIntent(this, clubId)
                .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT),
        )
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(ChatLifecycleObserver.getInstance())
    }

    companion object {
        private const val INVALID_ID = -1L
        private const val EXTRA_CHAT_ID = "chatId"

        private const val EXTRA_MEMBER_ID = "memberId"

        fun getIntent(
            context: Context,
            chatId: Long,
        ): Intent {
            if (context is FirebaseMessagingService) {
                AppModule.getInstance().analyticsHelper.logChatAlarmClicked()
            }
            return Intent(context, ChatActivity::class.java).apply {
                putExtra(EXTRA_CHAT_ID, chatId)
            }
        }
    }
}
