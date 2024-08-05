package com.happy.friendogly.presentation.ui.group.add

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.happy.friendogly.domain.usecase.GetAddressUseCase
import com.happy.friendogly.domain.usecase.PostClubUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.group.add.adapter.GroupAddAdapter.Companion.MAX_PAGE_SIZE
import com.happy.friendogly.presentation.ui.group.add.adapter.GroupAddAdapter.Companion.MIN_PAGE
import com.happy.friendogly.presentation.ui.group.add.model.GroupCounter
import com.happy.friendogly.presentation.ui.group.filter.GroupFilterItemActionHandler
import com.happy.friendogly.presentation.ui.group.model.GroupFilterSelector
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter

class GroupAddViewModel(
    private val getAddressUseCase: GetAddressUseCase,
    private val postClubUseCase: PostClubUseCase,
) : BaseViewModel(), GroupAddActionHandler, GroupFilterItemActionHandler {
    private val _groupAddEvent: MutableLiveData<Event<GroupAddEvent>> =
        MutableLiveData()
    val groupAddEvent: LiveData<Event<GroupAddEvent>> get() = _groupAddEvent

    private val _currentPage: MutableLiveData<Int> = MutableLiveData(MIN_PAGE)
    val currentPage: LiveData<Int> get() = _currentPage

    private val groupFilterSelector = GroupFilterSelector()

    private val _groupCounter: MutableLiveData<GroupCounter> = MutableLiveData(GroupCounter())
    val groupCounter: LiveData<GroupCounter> get() = _groupCounter

    val groupTitle: MutableLiveData<String> = MutableLiveData("")

    val groupContent: MutableLiveData<String> = MutableLiveData("")

    private val _groupPoster: MutableLiveData<Bitmap?> = MutableLiveData(null)
    val groupPoster: LiveData<Bitmap?> get() = _groupPoster

    val isValidNextPage: LiveData<Boolean>
        get() = MutableLiveData(
            when (currentPage.value) {
                0 -> isValidEditGroup()
                1 -> isValidFilterGroup()
                2 -> isValidGroupCount()
                else -> false
            }
        )

    val isValidPrevPage: LiveData<Boolean>
        get() = MutableLiveData(
            when (currentPage.value) {
                1, 2 -> true
                else -> false
            }
        )

    init {
        groupFilterSelector.initGroupFilter(GroupFilter.makeGroupFilterEntry())
    }

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

    fun settingGroupCounter(count: Int) {
        _groupCounter.value = GroupCounter(count)
    }

    fun updateGroupPoster(bitmap: Bitmap? = null) {
        _groupPoster.value = bitmap
    }

    override fun cancelAddGroup() {
        _groupAddEvent.emit(GroupAddEvent.Navigation.NavigateToHome)
    }

    // TODO : add api
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

    private fun isValidEditGroup(): Boolean {
        val groupTitleLength = groupTitle.value?.length ?: (MIN_TEXT_LENGTH - 1)
        val groupContentLength = groupContent.value?.length ?: (MIN_TEXT_LENGTH - 1)
        return groupTitleLength in MIN_TEXT_LENGTH..MAX_TITLE_LENGTH &&
                groupContentLength in MIN_TEXT_LENGTH..MAX_CONTENT_LENGTH
    }

    private fun isValidFilterGroup(): Boolean {
        return with(groupFilterSelector) {
            isContainSizeFilter() && isContainGenderFilter()
        }
    }

    private fun isValidGroupCount(): Boolean {
        return _groupCounter.value?.isValid() ?: false
    }

    companion object {
        private const val MIN_TEXT_LENGTH = 1
        private const val MAX_TITLE_LENGTH = 100
        private const val MAX_CONTENT_LENGTH = 1000

        fun factory(
            getAddressUseCase: GetAddressUseCase,
            postClubUseCase: PostClubUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                GroupAddViewModel(
                    getAddressUseCase = getAddressUseCase,
                    postClubUseCase = postClubUseCase,
                )
            }
        }
    }
}
