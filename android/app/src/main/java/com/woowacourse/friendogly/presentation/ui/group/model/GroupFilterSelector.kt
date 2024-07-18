package com.woowacourse.friendogly.presentation.ui.group.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.ParticipationFilter

class GroupFilterSelector {
    private val _participationFilter: MutableLiveData<ParticipationFilter> = MutableLiveData(
        ParticipationFilter.POSSIBLE)
    val participationFilter: LiveData<ParticipationFilter> get() = _participationFilter

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

    private fun confirmInValidFilter(filter: GroupFilter): Boolean {
        return currentSelectedFilters.value?.all {
            it.filterName != filter.filterName
        } ?: true
    }

    companion object {
        const val GENDER_FILTER_SUBJECT = "성별"
        const val SIZE_FILTER_SUBJECT = "사이즈"
    }
}
