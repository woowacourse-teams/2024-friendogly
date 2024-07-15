package com.woowacourse.friendogly.presentation.ui.mypage

import androidx.fragment.app.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentMyPageBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.base.observeEvent
import com.woowacourse.friendogly.presentation.ui.mypage.adapter.DogProfileAdapter

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels()

    private val adapter: DogProfileAdapter by lazy { DogProfileAdapter(viewModel) }

    override fun initViewCreated() {
        initDataBinding()
        initAdapter()
        initObserve()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initAdapter() {
        binding.rvDogProfile.adapter = adapter
    }

    private fun initObserve() {
        viewModel.navigateAction.observeEvent(this) { action ->
            when (action) {
                is MyPageNavigationAction.NavigateToSetting -> TODO()
                is MyPageNavigationAction.NavigateToDogDetail -> TODO()
                is MyPageNavigationAction.NavigateToDogRegister ->
                    navigate(MyPageFragmentDirections.actionMyPageFragmentToRegisterDogFragment())

                is MyPageNavigationAction.NavigateToProfileEdit -> TODO()
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            adapter.submitList(uiState.dogs)
        }
    }
}
