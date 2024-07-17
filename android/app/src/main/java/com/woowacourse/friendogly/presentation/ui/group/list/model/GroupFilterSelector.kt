package com.woowacourse.friendogly.presentation.ui.group.list.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.woowacourse.friendogly.presentation.ui.group.list.model.groupfilter.GroupFilter
import com.woowacourse.friendogly.presentation.ui.group.list.model.groupfilter.ParticipationFilter

class GroupFilterSelector {
    var participationFilter = ParticipationFilter.POSSIBLE
        private set

    private val _currentSelectedFilters: MutableLiveData<List<GroupFilter>> = MutableLiveData()
    val currentSelectedFilters: LiveData<List<GroupFilter>> get() = _currentSelectedFilters

    fun selectParticipationFilter(selectedFilter: ParticipationFilter) {
        participationFilter = selectedFilter
    }

    fun addGroupFilter(filter: GroupFilter) {
        _currentSelectedFilters.value = _currentSelectedFilters.value?.plus(filter)
    }

    fun removeGroupFilter(filter: GroupFilter) {
        _currentSelectedFilters.value = _currentSelectedFilters.value?.minus(filter)
    }

    companion object {
        const val GENDER_FILTER_SUBJECT = "성별"
        const val SIZE_FILTER_SUBJECT = "사이즈"
    }
}
