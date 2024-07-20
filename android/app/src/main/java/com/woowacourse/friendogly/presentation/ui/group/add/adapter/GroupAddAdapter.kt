package com.woowacourse.friendogly.presentation.ui.group.add.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.woowacourse.friendogly.presentation.ui.group.add.detal.GroupAddDetailFragment
import com.woowacourse.friendogly.presentation.ui.group.add.filter.GroupAddFilterFragment
import com.woowacourse.friendogly.presentation.ui.group.add.information.GroupAddInformationFragment

class GroupAddAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val groupAddFragments: List<Fragment> =
        listOf(
            GroupAddInformationFragment(),
            GroupAddFilterFragment(),
            GroupAddDetailFragment(),
        )

    override fun getItemCount(): Int {
        return groupAddFragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return if (position in groupAddFragments.indices) {
            groupAddFragments[position]
        } else {
            groupAddFragments[0]
        }
    }

    companion object {
        const val CURRENT_PAGE_SIZE =  3
        const val DEFAULT_PAGE = 0
    }
}
