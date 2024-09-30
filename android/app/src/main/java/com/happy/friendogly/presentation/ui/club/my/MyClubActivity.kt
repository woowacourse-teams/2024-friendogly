package com.happy.friendogly.presentation.ui.club.my

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityMyClubBinding
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.dialog.PetAddAlertDialog
import com.happy.friendogly.presentation.ui.club.add.ClubAddActivity
import com.happy.friendogly.presentation.ui.club.common.observeClubError
import com.happy.friendogly.presentation.ui.club.detail.ClubDetailActivity
import com.happy.friendogly.presentation.ui.club.my.adapter.MyClubAdapter
import com.happy.friendogly.presentation.ui.registerpet.RegisterPetActivity
import com.happy.friendogly.presentation.utils.logClubDetailClick
import com.happy.friendogly.presentation.utils.logMyAddClubClick
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyClubActivity :
    BaseActivity<ActivityMyClubBinding>(R.layout.activity_my_club), MyClubActionHandler {
    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val viewModel: MyClubViewModel by viewModels()

    private val adapter: MyClubAdapter by lazy {
        MyClubAdapter(this@MyClubActivity)
    }

    override fun initCreateView() {
        initDataBinding()
        initViewPager()
        initTabLayout()
        initPage()
        initObserver()
    }

    private fun initDataBinding() {
        binding.actionHandler = this
    }

    private fun initTabLayout() {
        TabLayoutMediator(binding.tlMyClub, binding.vpMyClub) { tab, position ->
            binding.vpMyClub.setCurrentItem(position, true)
            when (position) {
                0 -> tab.text = getString(R.string.my_club_subject)
                1 -> tab.text = getString(R.string.my_head_club_subject)
            }
        }.attach()
    }

    private fun initPage() {
        val isMyHead = receiveMyHead()
        updateClubFragment(isMyHead)
    }

    private fun initObserver() {
        viewModel.myClubEvent.observeEvent(this) { event ->
            when (event) {
                MyClubEvent.AddPet.OpenAddClub -> startActivity(ClubAddActivity.getIntent(this))

                MyClubEvent.AddPet.OpenAddPet -> openRegisterPetDialog()
            }
        }

        viewModel.clubErrorHandler.observeClubError(
            owner = this@MyClubActivity,
            sendSnackBar = { messageId -> showSnackbar(getString(messageId)) },
            sendToast = { messageId -> showToastMessage(getString(messageId)) }
        )
    }

    private fun openRegisterPetDialog() {
        PetAddAlertDialog(
            clickToNegative = {},
            clickToPositive = {
                startActivity(RegisterPetActivity.getIntent(this, null))
            },
        ).show(supportFragmentManager, "TAG")
    }

    private fun updateClubFragment(isMyHead: Boolean) {
        if (isMyHead) {
            binding.vpMyClub.setCurrentItem(1, true)
        } else {
            binding.vpMyClub.setCurrentItem(0, true)
        }
    }

    private fun receiveMyHead(): Boolean {
        return intent.getBooleanExtra(KEY_IS_MY_HEAD_CLUB, false)
    }

    private fun initViewPager() {
        binding.vpMyClub.adapter = adapter
    }

    override fun closeMyClub() {
        finish()
    }

    override fun addClub() {
        analyticsHelper.logMyAddClubClick()
        viewModel.loadPetState()
    }

    override fun openClub(clubId: Long) {
        analyticsHelper.logClubDetailClick()
        startActivity(ClubDetailActivity.getIntent(this, clubId))
    }

    companion object {
        const val KEY_IS_MY_HEAD_CLUB = "putIsMyHead"

        fun getIntent(
            context: Context,
            isMyHead: Boolean,
        ): Intent {
            return Intent(context, MyClubActivity::class.java).apply {
                putExtra(KEY_IS_MY_HEAD_CLUB, isMyHead)
            }
        }
    }
}
