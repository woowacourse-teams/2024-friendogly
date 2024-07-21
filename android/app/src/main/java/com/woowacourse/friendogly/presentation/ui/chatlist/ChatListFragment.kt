package com.woowacourse.friendogly.presentation.ui.chatlist

import androidx.fragment.app.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentChatListBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.ui.chatlist.adapter.ChatListAdapter
import com.woowacourse.friendogly.presentation.ui.chatlist.chat.ChatActivity

class ChatListFragment : BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list),
    ChatListNavigationAction {
    private val viewModel: ChatListViewModel by viewModels()
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
        startActivity(ChatActivity.getIntent(requireContext(), chatId))
    }
}
