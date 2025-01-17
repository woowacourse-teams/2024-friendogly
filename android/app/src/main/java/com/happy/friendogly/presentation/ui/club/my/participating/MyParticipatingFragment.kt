package com.happy.friendogly.presentation.ui.club.my.participating

import android.view.View
import androidx.fragment.app.viewModels
import com.happy.friendogly.R
import com.happy.friendogly.databinding.FragmentMyClubBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.club.common.ClubItemActionHandler
import com.happy.friendogly.presentation.ui.club.common.MessageHandler
import com.happy.friendogly.presentation.ui.club.common.handleError
import com.happy.friendogly.presentation.ui.club.my.MyClubActivity
import com.happy.friendogly.presentation.ui.club.my.MyClubEvent
import com.happy.friendogly.presentation.ui.club.my.MyClubUiState
import com.happy.friendogly.presentation.ui.club.my.adapter.MyClubListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyParticipatingFragment : BaseFragment<FragmentMyClubBinding>(R.layout.fragment_my_club) {
    private val viewModel: MyParticipatingClubViewModel by viewModels()

    private val adapter: MyClubListAdapter by lazy {
        MyClubListAdapter(viewModel as ClubItemActionHandler)
    }

    override fun initViewCreated() {
        initDataBinding()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadMyClubs()
    }

    private fun initDataBinding() {
        binding.actionHandler = viewModel
        binding.includeClubList.rcvClubListClub.adapter = adapter
    }

    private fun initObserver() {
        viewModel.myClubs.observe(viewLifecycleOwner) { clubs ->
            adapter.submitList(clubs)
        }

        viewModel.myClubEvent.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                MyClubEvent.Navigation.NavigateToAddClub -> (activity as MyClubActivity).addClub()
                is MyClubEvent.Navigation.NavigateToClub ->
                    (activity as MyClubActivity).openClub(
                        event.clubId,
                    )
            }
        }

        viewModel.myClubUiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                MyClubUiState.Error -> applyViewState(binding.includeClubError.linearLayoutClubError)
                MyClubUiState.Init -> applyViewState(binding.includeClubList.rcvClubListClub)
                MyClubUiState.NotData -> applyViewState(binding.includeClubData.linearLayoutClubNotData)
                MyClubUiState.Loading -> applyViewState(binding.includeClubLoading.nestedViewLayoutClubLoading)
            }
        }

        viewModel.clubErrorHandler.error.observeEvent(viewLifecycleOwner) {
            it.handleError { message ->
                when (message) {
                    is MessageHandler.SendSnackBar -> showSnackbar(getString(message.messageId))
                    is MessageHandler.SendToast -> showToastMessage(getString(message.messageId))
                }
            }
        }
    }

    private fun applyViewState(currentView: View) {
        binding.includeClubData.linearLayoutClubNotData.visibility = View.GONE
        binding.includeClubError.linearLayoutClubError.visibility = View.GONE
        binding.includeClubList.rcvClubListClub.visibility = View.GONE
        binding.includeClubLoading.nestedViewLayoutClubLoading.visibility = View.GONE
        currentView.visibility = View.VISIBLE
    }
}
