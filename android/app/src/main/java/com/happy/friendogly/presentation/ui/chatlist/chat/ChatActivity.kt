package com.happy.friendogly.presentation.ui.chatlist.chat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.messaging.FirebaseMessagingService
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityChatBinding
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.chatlist.ChatListFragment
import com.happy.friendogly.presentation.ui.chatlist.chat.adapter.ChatAdapter
import com.happy.friendogly.presentation.ui.chatlist.chatinfo.ChatInfoSideSheet
import com.happy.friendogly.presentation.ui.club.detail.ClubDetailActivity
import com.happy.friendogly.presentation.ui.otherprofile.OtherProfileActivity
import com.happy.friendogly.presentation.utils.hideKeyboard
import com.happy.friendogly.presentation.utils.logChatSendMessageClicked
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity :
    BaseActivity<ActivityChatBinding>(R.layout.activity_chat),
    ChatNavigationAction {
    private val viewModel: ChatViewModel by viewModels()
    private lateinit var adapter: ChatAdapter
    private val chatRoomId by lazy {
        intent.getLongExtra(EXTRA_CHAT_ID, INVALID_ID)
    }

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    override fun initCreateView() {
        binding.vm = viewModel
        lifecycle.addObserver(ChatLifecycleObserver.getInstance())
        initAdapter()
        addScrollListener()
        scrollTopOnNewChat()
        getChatList()
        clickBackBtn()
        clickChatInfo()
        clickSendMessage()
        viewModel.subscribeMessage(chatRoomId)
        hideMessageKeyBoard()
    }

    private fun clickSendMessage() {
        binding.ibChatSendMessage.setOnClickListener {
            viewModel.sendMessage(chatRoomId, binding.edtChatSendMessage.text.toString())
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

    private fun clickChatInfo() {
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

    private fun addScrollListener() {
        binding.rcvChatDetail.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(
                    recyclerView: RecyclerView,
                    newState: Int,
                ) {
                    super.onScrollStateChanged(recyclerView, newState)
                    reachEndOfMessages()
                }
            },
        )
    }

    private fun reachEndOfMessages() {
        if (!binding.rcvChatDetail.canScrollVertically(-1)) {
            viewModel.getChatMessage(
                chatRoomId = chatRoomId,
                currentPosition = adapter.itemCount,
            )
        }
    }

    private fun getChatList() {
        lifecycleScope.launch {
            viewModel.chats.collectLatest {
                adapter.submitList(it)
            }
        }
    }

    private fun scrollTopOnNewChat() {
        viewModel.newChatEvent.observeEvent(this) {
            binding.rcvChatDetail.post {
                binding.rcvChatDetail.smoothScrollToPosition(0)
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
