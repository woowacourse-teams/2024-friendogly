package com.happy.friendogly.presentation.ui.chatlist.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessagingService
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityChatBinding
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.ui.chatlist.ChatListFragment
import com.happy.friendogly.presentation.ui.chatlist.chat.adapter.ChatAdapter
import com.happy.friendogly.presentation.ui.chatlist.chatinfo.ChatInfoSideSheet
import com.happy.friendogly.presentation.ui.club.detail.ClubDetailActivity
import com.happy.friendogly.presentation.ui.otherprofile.OtherProfileActivity
import com.happy.friendogly.presentation.utils.hideKeyboard
import com.happy.friendogly.presentation.utils.logChatSendMessageClicked
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity :
    BaseActivity<ActivityChatBinding>(R.layout.activity_chat),
    ChatNavigationAction {
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var adapter: ChatAdapter

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

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
            analyticsHelper.logChatSendMessageClicked()
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

    override fun leaveChat() {
        setResult(ChatListFragment.LEAVE_CHAT_CODE, intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(ChatLifecycleObserver.getInstance())
    }

    companion object {
        // var analyticsHelper:AnalyticsHelper = AnalyticsHelper(this)

        private const val INVALID_ID = -1L
        private const val EXTRA_CHAT_ID = "chatId"

        private const val EXTRA_MEMBER_ID = "memberId"

        fun getIntent(
            context: Context,
            chatId: Long,
        ): Intent {
            if (context is FirebaseMessagingService) {
                // analyticsHelper.logChatAlarmClicked()
            }
            return Intent(context, ChatActivity::class.java).apply {
                putExtra(EXTRA_CHAT_ID, chatId)
            }
        }
    }
}
