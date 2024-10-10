package com.happy.friendogly.presentation.ui.chatlist

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.happy.friendogly.R
import com.happy.friendogly.databinding.FragmentChatListBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.ui.chatlist.adapter.ChatListAdapter
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatListFragment :
    BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list),
    ChatListNavigationAction {
    private lateinit var chatRegisterLauncher: ActivityResultLauncher<Intent>

    private val viewModel: ChatListViewModel by viewModels()
    private lateinit var adapter: ChatListAdapter

    override fun initViewCreated() {
        initAdapter()
        initChatResultLauncher()
        binding.vm = viewModel
    }

    override fun onStart() {
        super.onStart()
        viewModel.getChats()
    }

    private fun initAdapter() {
        adapter = ChatListAdapter(this)
        binding.rcvChatList.adapter = adapter
        viewModel.getChats()
        viewModel.chats.observe(viewLifecycleOwner) { chats ->
            adapter.submitList(chats)
        }
        swipeEvent()
    }

    private fun swipeEvent() {
        binding.swipelayoutChatListRefresh.setOnRefreshListener {
            viewModel.getChats()
            binding.swipelayoutChatListRefresh.isRefreshing = false
        }
    }

    override fun navigateToChat(chatId: Long) {
        chatRegisterLauncher.launch(ChatActivity.getIntent(requireContext(), chatId))
    }

    private fun initChatResultLauncher() {
        chatRegisterLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
            ) { result ->

                if (result.resultCode == Activity.RESULT_OK) {
                    viewModel.getChats()
                }
            }
    }

    companion object {
        const val TAG = "ChatListFragment"
        const val LEAVE_CHAT_CODE = 204
    }
}
