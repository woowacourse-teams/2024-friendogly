package com.woowacourse.friendogly.presentation.ui.group.add

import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.ui.group.model.GroupFilterSelector
import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

class GroupAddViewModel : BaseViewModel(), GroupAddActionHandler {
    val groupFilterSelector = GroupFilterSelector(
        groupList = GroupFilter.makeGroupFilterEntry()
    )

    override fun selectGroupFilter(
        filterName: String,
        isSelected: Boolean,
    ) {
        val groupFilter = GroupFilter.findGroupFilter(filterName) ?: return
        if (isSelected) {
            groupFilterSelector.addGroupFilter(groupFilter)
        } else {
            groupFilterSelector.removeGroupFilter(groupFilter)
        }
    }
}
