package com.happy.friendogly.presentation.ui.otherprofile

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.ActivityOtherProfileBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.dialog.AlertDialogModel
import com.happy.friendogly.presentation.dialog.DefaultRedAlertDialog
import com.happy.friendogly.presentation.ui.otherprofile.adapter.OtherPetProfileAdapter
import com.happy.friendogly.presentation.ui.otherprofile.bottom.BottomUserMore
import com.happy.friendogly.presentation.ui.otherprofile.bottom.BottomUserReport
import com.happy.friendogly.presentation.ui.otherprofile.bottom.UserMoreType

class OtherProfileActivity :
    BaseActivity<ActivityOtherProfileBinding>(R.layout.activity_other_profile) {
    private val viewModel: OtherProfileViewModel by viewModels {
        OtherProfileViewModel.factory(
            getPetsMineUseCase = AppModule.getInstance().getPetsMineUseCase,
            getMemberMineUseCase = AppModule.getInstance().getMemberMineUseCase,
        )
    }

    private val adapter: OtherPetProfileAdapter by lazy { OtherPetProfileAdapter(viewModel) }

    override fun initCreateView() {
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
                is OtherProfileNavigationAction.NavigateToBack -> finish()
                is OtherProfileNavigationAction.NavigateToDogDetail -> {}
                is OtherProfileNavigationAction.NavigateToUserMore -> moreBottomDialog(id = action.id)
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
