package com.happy.friendogly.presentation.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityMainBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.ui.chatlist.ChatListFragment
import com.happy.friendogly.presentation.ui.dogdetail.DogDetailActivity
import com.happy.friendogly.presentation.ui.group.add.GroupAddActivity
import com.happy.friendogly.presentation.ui.group.detail.GroupDetailActivity
import com.happy.friendogly.presentation.ui.group.list.GroupListFragment
import com.happy.friendogly.presentation.ui.mypage.MyPageFragment
import com.happy.friendogly.presentation.ui.registerdog.RegisterDogActivity
import com.happy.friendogly.presentation.ui.woof.WoofFragment

class MainActivity :
    BaseActivity<ActivityMainBinding>(R.layout.activity_main),
    MainActivityActionHandler {
    private var waitTime = 0L

    override fun initCreateView() {
        initNavController()
        requestLocationPermissions()
    }

    private fun initNavController() {
        switchFragment(GroupListFragment::class.java)
        binding.bottomNavi.setOnItemReselectedListener {}
        binding.bottomNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.groupListFragment -> switchFragment(GroupListFragment::class.java)
                R.id.woofFragment -> switchFragment(WoofFragment::class.java)
                R.id.chatListFragment -> switchFragment(ChatListFragment::class.java)
                R.id.myPageFragment -> switchFragment(MyPageFragment::class.java)
                else -> false
            }
        }
    }

    private fun switchFragment(fragmentClass: Class<out Fragment>): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(fragmentClass.simpleName)
        val transaction = supportFragmentManager.beginTransaction()

        supportFragmentManager.fragments.forEach {
            transaction.hide(it)
        }

        if (fragment == null) {
            transaction.add(
                R.id.nav_host_fragment_container,
                fragmentClass.getDeclaredConstructor().newInstance(),
                fragmentClass.simpleName,
            )
        } else {
            transaction.show(fragment)
        }
        transaction.setReorderingAllowed(true)
        transaction.commit()

        return true
    }

    private fun requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions =
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    override fun navigateToGroupDetailActivity(groupId: Long) {
        startActivity(GroupDetailActivity.getIntent(this, groupId))
    }

    override fun navigateToGroupAddActivity() {
        startActivity(GroupAddActivity.getIntent(this))
    }

    override fun navigateToRegisterDog() {
        startActivity(RegisterDogActivity.getIntent(this))
    }

    override fun navigateToDogDetail() {
        startActivity(DogDetailActivity.getIntent(this))
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() - waitTime >= 1500) {
            waitTime = System.currentTimeMillis()
            showToastMessage(getString(R.string.on_back_pressed_Message))
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 100

        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
