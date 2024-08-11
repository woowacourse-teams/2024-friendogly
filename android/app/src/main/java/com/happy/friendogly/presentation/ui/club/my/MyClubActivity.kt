package com.happy.friendogly.presentation.ui.club.my

import com.google.android.material.tabs.TabLayoutMediator
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityMyClubBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.ui.club.add.ClubAddActivity
import com.happy.friendogly.presentation.ui.club.detail.ClubDetailActivity
import com.happy.friendogly.presentation.ui.club.my.adapter.MyClubAdapter

class MyClubActivity :
    BaseActivity<ActivityMyClubBinding>(R.layout.activity_my_club), MyClubActionHandler {
    private val adapter: MyClubAdapter by lazy {
        MyClubAdapter(this@MyClubActivity)
    }

    override fun initCreateView() {
        initDataBinding()
        initViewPager()
        initTabLayout()
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

    private fun initViewPager() {
        binding.vpMyClub.adapter = adapter
    }

    override fun closeMyClub() {
        finish()
    }

    override fun addClub() {
        startActivity(ClubAddActivity.getIntent(this))
    }

    override fun openClub(clubId: Long) {
        startActivity(ClubDetailActivity.getIntent(this, clubId))
    }
}
