package com.happy.friendogly.presentation.ui

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.happy.friendogly.R
import com.happy.friendogly.databinding.ActivityMainBinding
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.ui.chatlist.ChatListFragment
import com.happy.friendogly.presentation.ui.club.add.ClubAddActivity
import com.happy.friendogly.presentation.ui.club.detail.ClubDetailActivity
import com.happy.friendogly.presentation.ui.club.list.ClubListFragment
import com.happy.friendogly.presentation.ui.club.my.MyClubActivity
import com.happy.friendogly.presentation.ui.mylocation.SettingMyLocationActivity
import com.happy.friendogly.presentation.ui.mypage.MyPageFragment
import com.happy.friendogly.presentation.ui.permission.MultiPermission
import com.happy.friendogly.presentation.ui.petdetail.PetDetailActivity
import com.happy.friendogly.presentation.ui.petdetail.PetsDetail
import com.happy.friendogly.presentation.ui.playground.PlaygroundFragment
import com.happy.friendogly.presentation.ui.profilesetting.ProfileSettingActivity
import com.happy.friendogly.presentation.ui.profilesetting.model.Profile
import com.happy.friendogly.presentation.ui.recentpet.RecentPetActivity
import com.happy.friendogly.presentation.ui.registerpet.RegisterPetActivity
import com.happy.friendogly.presentation.ui.registerpet.model.PetProfile
import com.happy.friendogly.presentation.ui.setting.SettingActivity
import com.happy.friendogly.presentation.utils.logChatListFragmentSwitched
import com.happy.friendogly.presentation.utils.logClubListFragmentSwitched
import com.happy.friendogly.presentation.utils.logMyClubClick
import com.happy.friendogly.presentation.utils.logMyPageFragmentSwitched
import com.happy.friendogly.presentation.utils.logPlaygroundFragmentSwitched
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity :
    BaseActivity<ActivityMainBinding>(R.layout.activity_main),
    MainActivityActionHandler {
    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private lateinit var permission: MultiPermission

    override fun initCreateView() {
        initNavController()
        permission =
            initMultiPermission().apply {
                showDialog().launch()
            }
    }

    private fun initMultiPermission() =
        MultiPermission.from(this, analyticsHelper).addAlarmPermission().addLocationPermission()
            .createRequest()

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        navigateToFragment(intent)
    }

    private fun navigateToFragment(intent: Intent) {
        val fragmentTag = intent.getStringExtra(EXTRA_FRAGMENT)
        when (fragmentTag) {
            PlaygroundFragment.TAG -> {
                binding.bottomNavi.selectedItemId = R.id.playgroundFragment
                switchFragment(PlaygroundFragment::class.java, PlaygroundFragment.TAG)
            }
        }
    }

    private fun initNavController() {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) ?: switchFragment(
            ClubListFragment::class.java,
            ClubListFragment.TAG,
        )
        binding.bottomNavi.setOnItemReselectedListener {}
        binding.bottomNavi.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.clubListFragment ->
                    switchFragment(
                        ClubListFragment::class.java,
                        ClubListFragment.TAG,
                    )

                R.id.playgroundFragment -> switchFragment(PlaygroundFragment::class.java, PlaygroundFragment.TAG)

                R.id.chatListFragment ->
                    switchFragment(
                        ChatListFragment::class.java,
                        ChatListFragment.TAG,
                    )

                R.id.myPageFragment ->
                    switchFragment(
                        MyPageFragment::class.java,
                        MyPageFragment.TAG,
                    )

                else -> false
            }
        }
    }

    private fun switchFragment(
        fragmentClass: Class<out Fragment>,
        tag: String,
    ): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(tag)
        val transaction = supportFragmentManager.beginTransaction()

        logFragmentSwitched(fragmentClass)

        supportFragmentManager.fragments.forEach {
            transaction.hide(it)
        }

        if (fragment == null) {
            transaction.add(
                R.id.nav_host_fragment_container,
                fragmentClass.getDeclaredConstructor().newInstance(),
                tag,
            )
        } else {
            transaction.show(fragment)
        }
        transaction.setReorderingAllowed(true)
        transaction.commit()

        return true
    }

    private fun logFragmentSwitched(fragmentClass: Class<out Fragment>) {
        when (fragmentClass.simpleName) {
            ClubListFragment::class.simpleName -> {
                analyticsHelper.logClubListFragmentSwitched()
            }

            PlaygroundFragment::class.simpleName -> {
                analyticsHelper.logPlaygroundFragmentSwitched()
            }

            ChatListFragment::class.simpleName -> {
                analyticsHelper.logChatListFragmentSwitched()
            }

            MyPageFragment::class.simpleName -> {
                analyticsHelper.logMyPageFragmentSwitched()
            }
        }
    }

    override fun navigateToClubDetailActivity(
        clubId: Long,
        resultLauncher: ActivityResultLauncher<Intent>,
    ) {
        resultLauncher.launch(ClubDetailActivity.getIntent(this, clubId))
    }

    override fun navigateToClubAddActivity(resultLauncher: ActivityResultLauncher<Intent>) {
        resultLauncher.launch(ClubAddActivity.getIntent(this))
    }

    override fun navigateToRegisterPet(petProfile: PetProfile?) {
        startActivity(RegisterPetActivity.getIntent(this, petProfile))
    }

    override fun navigateToProfileSetting(profile: Profile?) {
        startActivity(ProfileSettingActivity.getIntent(this, null, profile))
    }

    override fun navigateToPetDetail(
        currentPage: Int,
        petsDetail: PetsDetail,
    ) {
        startActivity(PetDetailActivity.getIntent(this, currentPage, petsDetail))
    }

    override fun navigateToSetting() {
        startActivity(SettingActivity.getIntent(this))
    }

    override fun navigateToSettingLocation(resultLauncher: ActivityResultLauncher<Intent>) {
        resultLauncher.launch(SettingMyLocationActivity.getIntent(this))
    }

    override fun navigateToMyClub(isMyHead: Boolean) {
        analyticsHelper.logMyClubClick()
        startActivity(MyClubActivity.getIntent(this, isMyHead))
    }

    override fun navigateToRecentPet() {
        startActivity(RecentPetActivity.getIntent(this))
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 100
        const val EXTRA_FRAGMENT = "Fragment"

        fun getIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
