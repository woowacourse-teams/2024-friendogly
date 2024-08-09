package com.happy.friendogly.presentation.ui.club.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happy.friendogly.presentation.ui.club.model.clubfilter.ClubFilter

class ClubFilterSelector {
    private val _currentSelectedFilters: MutableLiveData<List<ClubFilter>> =
        MutableLiveData()
    val currentSelectedFilters: LiveData<List<ClubFilter>> get() = _currentSelectedFilters

    fun addClubFilter(filter: ClubFilter) {
        if (confirmValidFilter(filter)) {
            _currentSelectedFilters.value = _currentSelectedFilters.value?.plus(filter)
        }
    }

    fun selectGenderFilters(): List<ClubFilter.GenderFilter> {
        return currentSelectedFilters.value?.filterIsInstance<ClubFilter.GenderFilter>()
            ?.ifEmpty {
                ClubFilter.makeGenderFilterEntry()
            } ?: ClubFilter.makeGenderFilterEntry()
    }

    fun selectSizeFilters(): List<ClubFilter.SizeFilter> {
        return currentSelectedFilters.value?.filterIsInstance<ClubFilter.SizeFilter>()
            ?.ifEmpty {
                ClubFilter.makeSizeFilterEntry()
            } ?: ClubFilter.makeSizeFilterEntry()
    }

    fun removeClubFilter(filter: ClubFilter) {
        if (!confirmValidFilter(filter)) {
            _currentSelectedFilters.value =
                _currentSelectedFilters.value?.filterNot { it.filterName == filter.filterName }
        }
    }

    fun initClubFilter(filters: List<ClubFilter>) {
        _currentSelectedFilters.value = filters
    }

    private fun confirmValidFilter(filter: ClubFilter): Boolean {
        return currentSelectedFilters.value?.none {
            it.filterName == filter.filterName
        } ?: true
    }

    fun isContainSizeFilter(): Boolean {
        return currentSelectedFilters.value?.any { it is ClubFilter.SizeFilter } ?: false
    }

    fun isContainGenderFilter(): Boolean {
        return currentSelectedFilters.value?.any { it is ClubFilter.GenderFilter } ?: false
    }

    fun addAllGenderFilter() {
        val genderFilters = ClubFilter.makeGenderFilterEntry()
        genderFilters.forEach {
            addClubFilter(it)
        }
    }

    fun addAllSizeFilter() {
        val sizeFilters = ClubFilter.makeSizeFilterEntry()
        sizeFilters.forEach {
            addClubFilter(it)
        }
    }
}
