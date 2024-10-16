package com.happy.friendogly.presentation.ui.club.my.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.happy.friendogly.presentation.ui.club.my.head.MyHeadClubFragment
import com.happy.friendogly.presentation.ui.club.my.participating.MyParticipatingFragment

class MyClubAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val myClubFragments: List<Fragment> =
        listOf(
            MyParticipatingFragment(),
            MyHeadClubFragment(),
        )

    override fun getItemCount(): Int {
        return myClubFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return if (position in myClubFragments.indices) {
            myClubFragments[position]
        } else {
            myClubFragments[0]
        }
    }
}
