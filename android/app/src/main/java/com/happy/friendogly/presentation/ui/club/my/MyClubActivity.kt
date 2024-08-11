package com.happy.friendogly.presentation.ui.club.my

import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityMyClubBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.ui.club.add.ClubAddActivity
import com.happy.friendogly.presentation.ui.club.detail.ClubDetailActivity

class MyClubActivity :
    BaseActivity<ActivityMyClubBinding>(R.layout.activity_my_club),MyClubActionHandler {

    override fun initCreateView() {
        initDataBinding()
    }

    private fun initDataBinding(){
        binding.actionHandler = this
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
