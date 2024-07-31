package com.happy.friendogly.presentation.ui.mypage

import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
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
        binding.vpDogProfile.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.updateCurrentPage(position)
                }
            },
        )
    }

    private fun initAdapter() {
        binding.vpDogProfile.adapter = adapter
    }

    private fun initObserve() {
        viewModel.navigateAction.observeEvent(this) { action ->
            when (action) {
                is MyPageNavigationAction.NavigateToSetting -> {}
                is MyPageNavigationAction.NavigateToDogDetail -> (activity as MainActivityActionHandler).navigateToDogDetail()

                is MyPageNavigationAction.NavigateToDogRegister -> (activity as MainActivityActionHandler).navigateToRegisterDog()

                is MyPageNavigationAction.NavigateToProfileEdit -> {}
                is MyPageNavigationAction.NavigateToMyClubManger -> {}
                is MyPageNavigationAction.NavigateToMyParticipation -> {}
                is MyPageNavigationAction.NavigateToPetEdit -> {}
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            adapter.submitList(uiState.pets)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPetMine()
        viewModel.fetchMemberMine()
        viewModel.updateCurrentPage(0)
        binding.vpDogProfile.setCurrentItem(0, false)
    }
}
