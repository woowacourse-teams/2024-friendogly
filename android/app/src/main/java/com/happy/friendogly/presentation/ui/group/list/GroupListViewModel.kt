package com.happy.friendogly.presentation.ui.group.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.mapper.toDomain
import com.happy.friendogly.domain.mapper.toGenders
import com.happy.friendogly.domain.mapper.toPresentation
import com.happy.friendogly.domain.mapper.toSizeTypes
import com.happy.friendogly.domain.model.UserAddress
import com.happy.friendogly.domain.usecase.GetAddressUseCase
import com.happy.friendogly.domain.usecase.GetSearchingClubsUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.group.model.GroupFilterSelector
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.happy.friendogly.presentation.ui.group.model.groupfilter.ParticipationFilter
import kotlinx.coroutines.launch

class GroupListViewModel(
    private val getAddressUseCase: GetAddressUseCase,
    private val searchingClubsUseCase: GetSearchingClubsUseCase,
) : BaseViewModel(), GroupListActionHandler {
    private val _uiState: MutableLiveData<GroupListUiState> =
        MutableLiveData(GroupListUiState.Init)
    val uiState: LiveData<GroupListUiState> get() = _uiState

    private val _myAddress: MutableLiveData<UserAddress> =
        MutableLiveData()
    val myAddress: LiveData<UserAddress> get() = _myAddress

    private val _participationFilter: MutableLiveData<ParticipationFilter> =
        MutableLiveData(ParticipationFilter.POSSIBLE)
    val participationFilter: LiveData<ParticipationFilter> get() = _participationFilter

    val groupFilterSelector = GroupFilterSelector()

    private val _groups: MutableLiveData<List<GroupListUiModel>> = MutableLiveData()
    val groups: LiveData<List<GroupListUiModel>> get() = _groups

    private val _groupListEvent: MutableLiveData<Event<GroupListEvent>> = MutableLiveData()
    val groupListEvent: LiveData<Event<GroupListEvent>> get() = _groupListEvent

    init {
        loadGroupWithAddress()
    }

    fun loadGroupWithAddress() {
        if (myAddress.value != null) {
            loadGroups()
        } else {
            loadAddress()
        }
    }

    private fun loadAddress() =
        viewModelScope.launch {
            getAddressUseCase()
                .onSuccess {
                    _myAddress.value = it
                    loadGroups()
                }
                .onFailure {
                    _uiState.value = GroupListUiState.NotAddress
                }
        }

    private fun loadGroups() =
        viewModelScope.launch {
            searchingClubsUseCase(
                filterCondition = participationFilter.value?.toDomain() ?: return@launch,
                address = myAddress.value?.toDomain() ?: return@launch,
                genderParams = groupFilterSelector.selectGenderFilters().toGenders(),
                sizeParams = groupFilterSelector.selectSizeFilters().toSizeTypes(),
            )
                .onSuccess {
                    if (it.isEmpty()) {
                        _uiState.value = GroupListUiState.NotData
                    } else {
                        _uiState.value = GroupListUiState.Init
                    }
                    _groups.value =
                        it.map { group ->
                            group.toPresentation()
                        }
                }
        }

    fun updateGroupFilter(filters: List<GroupFilter>) {
        groupFilterSelector.initGroupFilter(filters)
    }

    fun updateParticipationFilter(participationFilter: ParticipationFilter) {
        _participationFilter.value = participationFilter
    }

    override fun loadGroup(groupId: Long) {
        _groupListEvent.emit(GroupListEvent.OpenGroup(groupId))
    }

    override fun addGroup() {
        _groupListEvent.emit(GroupListEvent.Navigation.NavigateToAddGroup)
    }

    override fun selectParticipationFilter() {
        val participationFilter = participationFilter.value ?: return
        _groupListEvent.emit(GroupListEvent.OpenParticipationFilter(participationFilter))
    }

    override fun selectSizeFilter() {
        val filters = groupFilterSelector.currentSelectedFilters.value ?: emptyList()
        _groupListEvent.emit(
            GroupListEvent.OpenFilterSelector(
                groupFilterType = GroupFilter.SizeFilter.Init,
                groupFilters = filters,
            ),
        )
    }

    override fun selectGenderFilter() {
        val filters = groupFilterSelector.currentSelectedFilters.value ?: emptyList()
        _groupListEvent.emit(
            GroupListEvent.OpenFilterSelector(
                groupFilterType = GroupFilter.GenderFilter.Init,
                groupFilters = filters,
            ),
        )
    }

    override fun removeFilter(groupFilter: GroupFilter) {
        groupFilterSelector.removeGroupFilter(filter = groupFilter)
    }

    override fun addMyLocation() {
        _groupListEvent.emit(
            GroupListEvent.Navigation.NavigateToAddress,
        )
    }

    companion object {
        fun factory(
            getAddressUseCase: GetAddressUseCase,
            searchingClubsUseCase: GetSearchingClubsUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                GroupListViewModel(
                    getAddressUseCase = getAddressUseCase,
                    searchingClubsUseCase = searchingClubsUseCase,
                )
            }
        }
    }
}
