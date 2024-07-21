package com.woowacourse.friendogly.presentation.ui.group.add

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit
import com.woowacourse.friendogly.presentation.ui.group.add.adapter.GroupAddAdapter.Companion.MAX_PAGE_SIZE
import com.woowacourse.friendogly.presentation.ui.group.add.adapter.GroupAddAdapter.Companion.MIN_PAGE
import com.woowacourse.friendogly.presentation.ui.group.add.model.GroupCounter
import com.woowacourse.friendogly.presentation.ui.group.model.GroupFilterSelector
import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

class GroupAddViewModel : BaseViewModel(), GroupAddActionHandler {
    private val _groupAddEvent: MutableLiveData<Event<GroupAddEvent>> =
        MutableLiveData()
    val groupAddEvent: LiveData<Event<GroupAddEvent>> get() = _groupAddEvent

    private val _allSizeSelectState: MutableLiveData<Boolean> = MutableLiveData(true)
    val allSizeSelectState: LiveData<Boolean> get() = _allSizeSelectState

    private val _allGenderSelectState: MutableLiveData<Boolean> = MutableLiveData(true)
    val allGenderSelectState: LiveData<Boolean> get() = _allGenderSelectState


    private val _currentPage: MutableLiveData<Int> = MutableLiveData(MIN_PAGE)
    val currentPage: LiveData<Int> get() = _currentPage

    private val groupFilterSelector =
        GroupFilterSelector(groupList = GroupFilter.makeGroupFilterEntry())

    private val _groupCounter: MutableLiveData<GroupCounter> = MutableLiveData(GroupCounter())
    val groupCounter: LiveData<GroupCounter> get() = _groupCounter

    private val _groupTitle: MutableLiveData<String> = MutableLiveData()
    val groupTitle: LiveData<String> get() = _groupTitle

    private val _groupContent: MutableLiveData<String> = MutableLiveData()
    val groupContent: LiveData<String> get() = _groupContent

    private val _groupPoster: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val groupPoster: LiveData<Bitmap?> get() = _groupPoster

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

    override fun selectAllSizeFilter(isSelected: Boolean) {
        if (isSelected) {
            groupFilterSelector.addAllSizeFilter()
            _allSizeSelectState.value = true
        } else {
            _allSizeSelectState.value = false
        }
    }

    override fun selectAllGenderFilter(isSelected: Boolean) {
        if (isSelected) {
            groupFilterSelector.addAllGenderFilter()
            _allGenderSelectState.value = true
        } else {
            _allGenderSelectState.value = false
        }
    }

    fun settingGroupCounter(count: Int) {
        _groupCounter.value = GroupCounter(count)
    }

    fun updateGroupPoster(bitmap: Bitmap? = null) {
        _groupPoster.value = bitmap
    }

    override fun cancelAddGroup() {
        _groupAddEvent.emit(GroupAddEvent.Navigation.NavigateToHome)
    }

    //TODO : add api
    override fun submitAddGroup() {
        _groupAddEvent.emit(GroupAddEvent.Navigation.NavigateToHome)
    }

    override fun navigatePrevPage() {
        val currentPage = _currentPage.value ?: return
        val newPage = currentPage - 1
        if (newPage in MIN_PAGE until MAX_PAGE_SIZE) {
            _currentPage.value = newPage
            _groupAddEvent.emit(GroupAddEvent.ChangePage(newPage))
        }
    }

    override fun navigateNextPage() {
        val currentPage = _currentPage.value ?: return
        val newPage = currentPage + 1
        if (newPage in MIN_PAGE until MAX_PAGE_SIZE) {
            _currentPage.value = newPage
            _groupAddEvent.emit(GroupAddEvent.ChangePage(newPage))
        }
    }

    override fun selectGroupImage() {
        _groupAddEvent.emit(GroupAddEvent.Navigation.NavigateToSelectGroupPoster)
    }

}
