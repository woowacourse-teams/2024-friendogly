package com.happy.friendogly.presentation.ui.mylocation

import androidx.activity.viewModels
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.ActivitySettingMyLocationBinding
import com.happy.friendogly.presentation.base.BaseActivity

class SettingMyLocationActivity :
    BaseActivity<ActivitySettingMyLocationBinding>(R.layout.activity_setting_my_location) {

    private val viewModel: SettingMyLocationViewModel by viewModels()

    override fun initCreateView() {
        initDataBinding()
        initObserver()
    }

    private fun initDataBinding() {
    }

    private fun initObserver(){

    }
}
