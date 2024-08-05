package com.happy.friendogly.presentation.ui.group.add

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.model.UserAddress
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
import com.happy.friendogly.presentation.ui.group.list.GroupListUiState
import com.happy.friendogly.presentation.ui.group.model.GroupFilterSelector
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.happy.friendogly.presentation.utils.addSourceList
import kotlinx.coroutines.launch

class GroupAddViewModel(
    private val getAddressUseCase: GetAddressUseCase,
    private val postClubUseCase: PostClubUseCase,
) : BaseViewModel(), GroupAddActionHandler, GroupFilterItemActionHandler {
    private val _myAddress: MutableLiveData<UserAddress> = MutableLiveData()
    val myAddress: LiveData<UserAddress> get() = _myAddress

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

    val isValidNextPage: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
        .apply {
            addSourceList(
                groupTitle,
                groupContent,
                groupFilterSelector.currentSelectedFilters,
                groupCounter,
                myAddress,
            ){
               isValidAddress()
            }
        }


    val isValidPrevPage:
            MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
        .apply {
            addSourceList(currentPage) {
                isValidPrevPage()
            }
        }

    init {
        loadAddress()
        initAllFilter()
    }

    fun settingGroupCounter(count: Int) {
        _groupCounter.value = GroupCounter(count)
    }

    fun updateGroupPoster(bitmap: Bitmap? = null) {
        _groupPoster.value = bitmap
    }

    private fun initAllFilter() {
        groupFilterSelector.initGroupFilter(GroupFilter.makeGroupFilterEntry())
    }

    private fun loadAddress() =
        viewModelScope.launch {
            getAddressUseCase()
                .onSuccess {
                    _myAddress.value = it
                }
                .onFailure {
                    _groupAddEvent.emit(GroupAddEvent.FailLoadAddress)
                }
        }

    private fun isValidAddedData(): Boolean{
        return isValidEditGroup() && isValidFilterGroup() && isValidGroupCount() && isValidAddress()
    }

    private fun isValidPage(page: Int): Boolean {
        return page in MIN_PAGE until MAX_PAGE_SIZE
    }

    private fun isValidPrevPage(): Boolean {
        val page = currentPage.value ?: MIN_PAGE
        return page in MIN_PAGE + 1 until MAX_PAGE_SIZE
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
        return groupCounter.value?.isValid() ?: false
    }

    private fun isValidAddress(): Boolean{
        return myAddress.value != null
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
        if (isValidPage(newPage)) {
            _currentPage.value = newPage
            _groupAddEvent.emit(GroupAddEvent.ChangePage(newPage))
        }
    }

    override fun selectGroupImage() {
        _groupAddEvent.emit(GroupAddEvent.Navigation.NavigateToSelectGroupPoster)
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
