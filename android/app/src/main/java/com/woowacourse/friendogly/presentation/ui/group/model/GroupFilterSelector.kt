package com.woowacourse.friendogly.presentation.ui.group.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

class GroupFilterSelector(
    groupList: List<GroupFilter> = listOf(),
) {
    private val _currentSelectedFilters: MutableLiveData<List<GroupFilter>> =
        MutableLiveData(groupList)
    val currentSelectedFilters: LiveData<List<GroupFilter>> get() = _currentSelectedFilters

    fun addGroupFilter(filter: GroupFilter) {
        if (confirmValidFilter(filter)) {
            _currentSelectedFilters.value = _currentSelectedFilters.value?.plus(filter)
        }
    }

    fun removeGroupFilter(filter: GroupFilter) {
        if (!confirmValidFilter(filter)) {
            _currentSelectedFilters.value =
                _currentSelectedFilters.value?.filterNot { it.filterName == filter.filterName }
        }
    }

    private fun confirmValidFilter(filter: GroupFilter): Boolean {
        return currentSelectedFilters.value?.none {
            it.filterName == filter.filterName
        } ?: true
    }

    fun addAllGenderFilter() {
        val genderFilters = GroupFilter.makeGenderFilterEntry()
        genderFilters.forEach {
            addGroupFilter(it)
        }
    }

    fun addAllSizeFilter() {
        val sizeFilters = GroupFilter.makeSizeFilterEntry()
        sizeFilters.forEach {
            addGroupFilter(it)
        }
    }

    companion object {
        const val GENDER_FILTER_SUBJECT = "성별"
        const val SIZE_FILTER_SUBJECT = "사이즈"
    }
}
