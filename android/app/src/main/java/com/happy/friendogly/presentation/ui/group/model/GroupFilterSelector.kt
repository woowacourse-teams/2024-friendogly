package com.happy.friendogly.presentation.ui.group.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

class GroupFilterSelector {
    private val _currentSelectedFilters: MutableLiveData<List<GroupFilter>> =
        MutableLiveData()
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

    fun initGroupFilter(filters: List<GroupFilter>) {
        _currentSelectedFilters.value = filters
    }

    private fun confirmValidFilter(filter: GroupFilter): Boolean {
        return currentSelectedFilters.value?.none {
            it.filterName == filter.filterName
        } ?: true
    }

    fun isContainSizeFilter(): Boolean {
        return currentSelectedFilters.value?.any { it is GroupFilter.SizeFilter } ?: false
    }

    fun isContainGenderFilter(): Boolean {
        return currentSelectedFilters.value?.any { it is GroupFilter.GenderFilter } ?: false
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
}
