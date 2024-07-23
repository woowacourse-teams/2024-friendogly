package com.woowacourse.friendogly.presentation.ui.mypage

import androidx.fragment.app.viewModels
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.application.di.AppModule
import com.woowacourse.friendogly.databinding.FragmentMyPageBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.base.observeEvent
import com.woowacourse.friendogly.presentation.ui.mypage.adapter.PetProfileAdapter

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels {
        MyPageViewModel.factory(getPetsMineUseCase = AppModule.getInstance().getPetsMineUseCase)
    }

    private val adapter: PetProfileAdapter by lazy { PetProfileAdapter(viewModel) }

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
                is MyPageNavigationAction.NavigateToDogDetail ->
                    navigate(
                        MyPageFragmentDirections.actionMyPageFragmentToDogDetailFragment(
                            action.id,
                        ),
                    )

                is MyPageNavigationAction.NavigateToDogRegister ->
                    navigate(
                        MyPageFragmentDirections.actionMyPageFragmentToRegisterDogFragment(),
                    )

                is MyPageNavigationAction.NavigateToProfileEdit -> TODO()
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            adapter.submitList(uiState.pets)
        }
    }
}
