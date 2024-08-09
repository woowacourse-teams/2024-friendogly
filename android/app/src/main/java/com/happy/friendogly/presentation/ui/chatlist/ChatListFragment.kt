package com.happy.friendogly.presentation.ui.chatlist

import androidx.fragment.app.viewModels
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.FragmentChatListBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.ui.chatlist.adapter.ChatListAdapter
import com.happy.friendogly.presentation.ui.chatlist.chat.ChatActivity

class ChatListFragment :
    BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list),
    ChatListNavigationAction {
    private val viewModel: ChatListViewModel by viewModels {
        ChatListViewModel.factory(
            AppModule.getInstance().chatRepository,
        )
    }
    private lateinit var adapter: ChatListAdapter

    override fun initViewCreated() {
        initAdapter()
    }

    private fun initAdapter() {
        adapter = ChatListAdapter(this)
        binding.rcvChatList.adapter = adapter
        viewModel.getChats()
        viewModel.chats.observe(viewLifecycleOwner) { chats ->
            adapter.submitList(chats)
        }
    }

    override fun navigateToChat(chatId: Long) {
        startActivity(ChatActivity.getIntent(requireContext(), chatId, viewModel.memberId))
    }
}
