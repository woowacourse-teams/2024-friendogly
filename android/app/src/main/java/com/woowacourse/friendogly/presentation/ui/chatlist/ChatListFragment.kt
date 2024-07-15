package com.woowacourse.friendogly.presentation.ui.chatlist

import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentChatListBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment

class ChatListFragment : BaseFragment<FragmentChatListBinding>(R.layout.fragment_chat_list) {
    override fun initViewCreated() {

        binding.test.setOnClickListener {
            val bottomSheet =  WoofBottomSheet()
            val bundle = WoofBottomSheet.getBundle(WoofDogUiModel("https://t1.daumcdn.net/thumb/R720x0.fjpg/?fname=http://t1.daumcdn.net/brunch/service/user/cnoC/image/PTcGsuuqjlyY1d9MxFkG7RAndmo.jpg",
                "땡이","소형견",2,"땡이 닉네임이랑 땡이 강아지 이름 똑가틈"))
            bottomSheet.arguments = bundle
            bottomSheet.show(parentFragmentManager, "")
        }
    }




}
