package com.woowacourse.friendogly.presentation.ui.group.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit
import com.woowacourse.friendogly.presentation.ui.group.add.adapter.GroupAddAdapter.Companion.DEFAULT_PAGE
import com.woowacourse.friendogly.presentation.ui.group.add.model.GroupCounter
import com.woowacourse.friendogly.presentation.ui.group.model.GroupFilterSelector
import com.woowacourse.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import okhttp3.MultipartBody

class GroupAddViewModel : BaseViewModel(), GroupAddActionHandler {
    private val _navigateAction: MutableLiveData<Event<GroupAddEvent>> =
            MutableLiveData()
    val navigationAction: LiveData<Event<GroupAddEvent>> get() = _navigateAction

    private val _currentPage: MutableLiveData<Int> = MutableLiveData(DEFAULT_PAGE)
    val currentPage: LiveData<Int> get() = _currentPage

    private val groupFilterSelector =
        GroupFilterSelector(groupList = GroupFilter.makeGroupFilterEntry())

    private val _groupCounter: MutableLiveData<GroupCounter> = MutableLiveData()
    val groupCounter: LiveData<GroupCounter> get() = _groupCounter

    private val _groupTitle: MutableLiveData<String> = MutableLiveData()
    val groupTitle: LiveData<String> get() = _groupTitle

    private val _groupContent: MutableLiveData<String> = MutableLiveData()
    val groupContent: LiveData<String> get() = _groupContent

    private val groupPoster: MultipartBody.Part? = null

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
        groupFilterSelector.addAllSizeFilter()
    }

    override fun selectAllGenderFilter(isSelected: Boolean) {
        groupFilterSelector.addAllGenderFilter()
    }

    override fun settingGroupCounter(count: Int) {
        _groupCounter.value = GroupCounter(count)
    }

    override fun cancelAddGroup() {
        _navigateAction.emit(GroupAddEvent.Navigation.NavigateToHome)
    }

    //TODO : add api
    override fun submitAddGroup() {
        _navigateAction.emit(GroupAddEvent.Navigation.NavigateToHome)
    }

    override fun navigatePrevPage() {
        TODO("Not yet implemented")
    }

    override fun navigateNextPage() {
        TODO("Not yet implemented")
    }

    private fun isValidInformationPage(){

    }

}
