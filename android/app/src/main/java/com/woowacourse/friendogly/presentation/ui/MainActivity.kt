package com.woowacourse.friendogly.presentation.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.woowacourse.friendogly.NavigationGraphDirections
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.ActivityMainBinding
import com.woowacourse.friendogly.presentation.base.BaseActivity
import com.woowacourse.friendogly.presentation.ui.group.add.GroupAddActivity
import com.woowacourse.friendogly.presentation.ui.group.detail.GroupDetailActivity

class MainActivity :
    BaseActivity<ActivityMainBinding>(R.layout.activity_main),
    MainActivityActionHandler {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private var waitTime = 0L

    override fun initCreateView() {
        initNavController()
        requestLocationPermissions()
    }

    private fun initNavController() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.groupListFragment, R.id.woofFragment, R.id.chatListFragment, R.id.myPageFragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }
        binding.bottomNavi.setupWithNavController(navController)
        binding.bottomNavi.setOnItemReselectedListener {}
    }

    private fun showBottomNav() {
        binding.bottomNavi.visibility = View.VISIBLE
    }

    private fun hideBottomNav() {
        binding.bottomNavi.visibility = View.GONE
    }

    override fun onBackPressed() {
        try {
            if (onBackPressedDispatcher.hasEnabledCallbacks()) {
                super.onBackPressed()
            } else {
                when (navController.currentDestination?.id) {
                    R.id.groupListFragment -> {
                        if (System.currentTimeMillis() - waitTime >= 1500) {
                            waitTime = System.currentTimeMillis()
                            showToastMessage(getString(R.string.on_back_pressed_Message))
                        } else {
                            finishAffinity()
                        }
                    }

                    null -> super.onBackPressed()
                    else -> navController.navigate(NavigationGraphDirections.actionHomeFragment())
                }
            }
        } catch (e: Exception) {
            showToastMessage(e.message.toString())
        }
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

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 100

        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun navigateToGroupDetailActivity(groupId: Long) {
        startActivity(GroupDetailActivity.getIntent(this, groupId))
    }

    override fun navigateToGroupAddActivity() {
        startActivity(GroupAddActivity.getIntent(this))
    }
}
