package com.woowacourse.friendogly.presentation.ui.chatlist

import androidx.fragment.app.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentChatListBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment

class ChatListFragment : BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list) {

    private val viewModel:WoofViewModel by viewModels()

    override fun initViewCreated() {

        binding.test.setOnClickListener {
            viewModel.getDogInfo(1)
        }

        viewModel.dogInfo.observe(viewLifecycleOwner) {
            val bottomSheet =  WoofBottomSheet()
            val bundle = WoofBottomSheet.getBundle(it)
            bottomSheet.arguments = bundle
            bottomSheet.show(parentFragmentManager, "")
        }
    }




}
