package com.happy.friendogly.presentation.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.viewModels
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.ActivitySettingBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.dialog.AlertDialogModel
import com.happy.friendogly.presentation.dialog.DefaultCoralAlertDialog
import com.happy.friendogly.presentation.dialog.DefaultRedAlertDialog
import com.happy.friendogly.presentation.dialog.LoadingDialog
import com.happy.friendogly.presentation.ui.MainActivity
import com.happy.friendogly.presentation.ui.register.RegisterActivity

class SettingActivity : BaseActivity<ActivitySettingBinding>(R.layout.activity_setting) {
    val viewModel: SettingViewModel by viewModels {
        SettingViewModel.factory(
            deleteTokenUseCase = AppModule.getInstance().deleteTokenUseCase,
        )
    }

    private val loadingDialog: LoadingDialog by lazy { LoadingDialog(this) }

    override fun initCreateView() {
        initDataBinding()
        initObserve()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initObserve() {
        viewModel.navigateAction.observeEvent(this) { action ->
            when (action) {
                is SettingNavigationAction.NavigateToBack -> finish()
                is SettingNavigationAction.NavigateToAppInfo ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(APP_INFO_URL)))

                is SettingNavigationAction.NavigateToPrivacyPolicy -> showSnackbar("준비중이에요")
                is SettingNavigationAction.NavigateToLogout -> logOutDialog()
                is SettingNavigationAction.NavigateToUnsubscribe -> userDeleteDialog()
                is SettingNavigationAction.NavigateToRegister -> {
                    startActivity(RegisterActivity.getIntent(this))
                    finish()
                }
            }
        }

        viewModel.message.observeEvent(this) { message ->
            when (message) {
                is SettingMessage.ServerErrorMessage -> showToastMessage(getString(R.string.server_error_message))
                is SettingMessage.DefaultErrorMessage -> showToastMessage(getString(R.string.default_error_message))
                is SettingMessage.TokenNotStoredErrorMessage ->
                    startActivity(MainActivity.getIntent(this))
            }
        }

        viewModel.loading.observeEvent(this) { loading ->
            if (loading) {
                showLoadingDialog()
            } else {
                dismissLoadingDialog()
            }
        }
    }

    private fun logOutDialog() {
        val alertDialogModel =
            AlertDialogModel(
                title = getString(R.string.logout_title),
                description = getString(R.string.logout_description),
                positiveContents = getString(R.string.logout),
                negativeContents = getString(R.string.no),
            )

        val dialog =
            DefaultCoralAlertDialog(
                alertDialogModel = alertDialogModel,
                clickToPositive = {
                    viewModel.navigateToLogout()
                    showToastMessage(getString(R.string.see_you_later_message))
                },
                clickToNegative = {},
            )
        dialog.show(supportFragmentManager, "TAG")
    }

    private fun userDeleteDialog() {
        val res =
            AlertDialogModel(
                title = getString(R.string.user_delete_title),
                description = getString(R.string.user_delete_description),
                positiveContents = getString(R.string.user_delete_yes),
                negativeContents = getString(R.string.no),
            )
        val dialog =
            DefaultRedAlertDialog(
                alertDialogModel = res,
                clickToPositive = {
                    viewModel.navigateToUnsubscribe()
                    showToastMessage(getString(R.string.withdrawn_message))
                },
                clickToNegative = {},
            )
        dialog.show(supportFragmentManager, "TAG")
    }

    private fun showLoadingDialog() {
        loadingDialog.show()
    }

    private fun dismissLoadingDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

    companion object {
        private const val APP_INFO_URL = "https://github.com/woowacourse-teams/2024-friendogly"

        fun getIntent(context: Context): Intent {
            return Intent(context, SettingActivity::class.java)
        }
    }
}
