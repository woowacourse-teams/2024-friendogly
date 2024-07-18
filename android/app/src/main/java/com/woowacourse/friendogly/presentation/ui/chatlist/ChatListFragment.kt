package com.woowacourse.friendogly.presentation.ui.chatlist

import androidx.fragment.app.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentChatListBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.ui.chatlist.adapter.ChatListAdapter

class ChatListFragment : BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list) {
    private val viewModel: ChatListViewModel by viewModels()
    private lateinit var adapter: ChatListAdapter

    override fun initViewCreated() {
        adapter = ChatListAdapter()
        binding.rcvChatList.adapter = adapter
        viewModel.getChats()
        viewModel.chats.observe(viewLifecycleOwner) { chats ->
            adapter.submitList(chats)
        }
    }
}
