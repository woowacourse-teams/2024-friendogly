package com.happy.friendogly.presentation.ui.otherprofile

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityOtherProfileBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.dialog.AlertDialogModel
import com.happy.friendogly.presentation.dialog.DefaultRedAlertDialog
import com.happy.friendogly.presentation.ui.otherprofile.adapter.OtherPetProfileAdapter
import com.happy.friendogly.presentation.ui.otherprofile.bottom.BottomUserMore
import com.happy.friendogly.presentation.ui.otherprofile.bottom.BottomUserReport
import com.happy.friendogly.presentation.ui.otherprofile.bottom.UserMoreType
import com.happy.friendogly.presentation.ui.petdetail.PetDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherProfileActivity :
    BaseActivity<ActivityOtherProfileBinding>(R.layout.activity_other_profile) {
    private val viewModel: OtherProfileViewModel by viewModels()

    private val adapter: OtherPetProfileAdapter by lazy { OtherPetProfileAdapter(viewModel) }

    override fun initCreateView() {
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
        viewModel.navigateAction.observeEvent(this) { action ->
            when (action) {
                is OtherProfileNavigationAction.NavigateToBack -> finish()
                is OtherProfileNavigationAction.NavigateToPetDetail ->
                    startActivity(
                        PetDetailActivity.getIntent(this, action.currentPage, action.petsDetail),
                    )

                is OtherProfileNavigationAction.NavigateToUserMore -> moreBottomDialog(id = action.id)
            }
        }

        viewModel.message.observeEvent(this) { message ->
            when (message) {
                is OtherProfileMessage.ServerErrorMessage -> showToastMessage(getString(R.string.server_error_message))
                is OtherProfileMessage.DefaultErrorMessage -> showToastMessage(getString(R.string.default_error_message))
                is OtherProfileMessage.NoInternetMessage -> showSnackbar(getString(R.string.no_internet_message))
            }
        }

        viewModel.uiState.observe(this) { uiState ->
            adapter.submitList(uiState.pets)
        }
    }

    private fun moreBottomDialog(id: Long) {
        val dialog =
            BottomUserMore { type ->
                when (type) {
                    is UserMoreType.UserDeclare -> usersBlockDialog(id = id)
                    is UserMoreType.Report -> reportDialog(id = id)
                }
            }
        dialog.show(supportFragmentManager, "TAG")
    }

    private fun usersBlockDialog(id: Long) {
        val alertDialogModel =
            AlertDialogModel(
                title = getString(R.string.user_block_title),
                description = getString(R.string.user_block_description),
                positiveContents = getString(R.string.user_block_yes),
                negativeContents = getString(R.string.user_block_no),
            )
        val dialog =
            DefaultRedAlertDialog(
                alertDialogModel = alertDialogModel,
                clickToPositive = { showSnackbar(getString(R.string.user_block_message)) },
                clickToNegative = {},
            )
        dialog.show(supportFragmentManager, "TAG")
    }

    private fun reportDialog(id: Long) {
        val bottomSheet =
            BottomUserReport(onSaved = { reportType -> showSnackbar(getString(R.string.user_report_message)) })
        bottomSheet.show(supportFragmentManager, "TAG")
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchPetMine()
        viewModel.updateCurrentPage(binding.vpPetProfile.currentItem)
    }

    companion object {
        const val PUT_EXTRA_USER_ID = "user_id"

        fun getIntent(
            context: Context,
            id: Long,
        ): Intent {
            return Intent(context, OtherProfileActivity::class.java).apply {
                putExtra(PUT_EXTRA_USER_ID, id)
            }
        }
    }
}
