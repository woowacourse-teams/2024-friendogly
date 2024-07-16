package com.woowacourse.friendogly.presentation.ui.group.select

import androidx.fragment.app.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentDogSelectBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment

class DogSelectFragment : BaseFragment<FragmentDogSelectBinding>(R.layout.fragment_dog_select) {
    private val viewModel: DogSelectViewModel by viewModels()
    override fun initViewCreated() {
        binding.vm = viewModel
    }
}
