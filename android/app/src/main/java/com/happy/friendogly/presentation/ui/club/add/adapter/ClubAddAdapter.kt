package com.happy.friendogly.presentation.ui.club.add.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.happy.friendogly.presentation.ui.club.add.detail.ClubAddDetailFragment
import com.happy.friendogly.presentation.ui.club.add.filter.ClubAddFilterFragment
import com.happy.friendogly.presentation.ui.club.add.information.ClubAddInformationFragment

class ClubAddAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val clubAddFragments: List<Fragment> =
        listOf(
            ClubAddInformationFragment(),
            ClubAddFilterFragment(),
            ClubAddDetailFragment(),
        )

    override fun getItemCount(): Int {
        return clubAddFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return if (position in clubAddFragments.indices) {
            clubAddFragments[position]
        } else {
            clubAddFragments[0]
        }
    }

    companion object {
        const val MAX_PAGE_SIZE = 3
        const val MIN_PAGE = 0
    }
}
