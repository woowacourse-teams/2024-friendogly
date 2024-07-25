package com.happy.friendogly.presentation.ui.mypage

import androidx.fragment.app.viewModels
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.FragmentMyPageBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.MainActivityActionHandler
import com.happy.friendogly.presentation.ui.mypage.adapter.PetProfileAdapter

class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels {
        MyPageViewModel.factory(
            getPetsMineUseCase = AppModule.getInstance().getPetsMineUseCase,
            getMemberMineUseCase = AppModule.getInstance().getMemberMineUseCase,
        )
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
                is MyPageNavigationAction.NavigateToDogDetail -> (activity as MainActivityActionHandler).navigateToDogDetail()

                is MyPageNavigationAction.NavigateToDogRegister -> (activity as MainActivityActionHandler).navigateToRegisterDog()

                is MyPageNavigationAction.NavigateToProfileEdit -> TODO()
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            adapter.submitList(uiState.pets)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPetMine()
    }
}
