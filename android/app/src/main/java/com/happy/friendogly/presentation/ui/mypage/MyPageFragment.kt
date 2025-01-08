package com.happy.friendogly.presentation.ui.mypage

import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.happy.friendogly.R
import com.happy.friendogly.databinding.FragmentMyPageBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.MainActivityActionHandler
import com.happy.friendogly.presentation.ui.mypage.adapter.PetProfileAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val viewModel: MyPageViewModel by viewModels()

    private val adapter: PetProfileAdapter by lazy { PetProfileAdapter(viewModel) }

    override fun initViewCreated() {
        initDataBinding()
        initAdapter()
        initObserve()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
        binding.vpPetProfile.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.updateCurrentPage(position)
                }
            },
        )
    }

    private fun initAdapter() {
        binding.vpPetProfile.adapter = adapter
    }

    private fun initObserve() {
        viewModel.navigateAction.observeEvent(viewLifecycleOwner) { action ->
            when (action) {
                is MyPageNavigationAction.NavigateToSetting -> (activity as MainActivityActionHandler).navigateToSetting()
                is MyPageNavigationAction.NavigateToPetDetail ->
                    (activity as MainActivityActionHandler).navigateToPetDetail(
                        currentPage = action.currentPage,
                        petsDetail = action.petsDetail,
                    )

                is MyPageNavigationAction.NavigateToDogRegister ->
                    (activity as MainActivityActionHandler).navigateToRegisterPet(null)

                is MyPageNavigationAction.NavigateToProfileEdit ->
                    (activity as MainActivityActionHandler).navigateToProfileSetting(profile = action.profile)

                is MyPageNavigationAction.NavigateToMyClubManger ->
                    (activity as MainActivityActionHandler).navigateToMyClub(isMyHead = true)

                is MyPageNavigationAction.NavigateToMyParticipation ->
                    (activity as MainActivityActionHandler).navigateToMyClub(isMyHead = false)

                is MyPageNavigationAction.NavigateToPetEdit ->
                    (activity as MainActivityActionHandler).navigateToRegisterPet(action.petProfile)

                is MyPageNavigationAction.NavigateToRecentPet -> (activity as MainActivityActionHandler).navigateToRecentPet()
            }
        }

        viewModel.message.observeEvent(viewLifecycleOwner) { message ->
            when (message) {
                is MyPageMessage.ServerErrorMessage -> showToastMessage(getString(R.string.server_error_message))
                is MyPageMessage.DefaultErrorMessage -> showToastMessage(getString(R.string.default_error_message))
                is MyPageMessage.NoInternetMessage -> showSnackbar(getString(R.string.no_internet_message))
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            adapter.submitList(uiState.pets) {
                binding.vpPetProfile.post {
                    binding.vpPetProfile.setCurrentItem(0, false)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPetMine()
        viewModel.fetchMemberMine()
        viewModel.updateCurrentPage(binding.vpPetProfile.currentItem)
    }

    companion object {
        const val TAG = "MyPageFragment"
    }
}
