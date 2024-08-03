package com.happy.friendogly.presentation.ui.group.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.model.UserAddress
import com.happy.friendogly.domain.usecase.GetAddressUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.group.model.GroupFilterSelector
import com.happy.friendogly.presentation.ui.group.model.groupfilter.GroupFilter
import com.happy.friendogly.presentation.ui.group.model.groupfilter.ParticipationFilter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class GroupListViewModel(
    private val getAddressUseCase: GetAddressUseCase,
) : BaseViewModel(), GroupListActionHandler {
    private val _uiState: MutableLiveData<GroupListUiState> =
        MutableLiveData(GroupListUiState.Init)
    val uiState: MutableLiveData<GroupListUiState> get() = _uiState

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

    fun loadGroups() = viewModelScope.launch {
        delay(1000)
        val groupData = dummy()
        if (groupData.isEmpty()) {
            _uiState.value = GroupListUiState.NotData
        } else {
            _uiState.value = GroupListUiState.Init
        }
        _groups.value = groupData
    }

    private fun loadGroupWithAddress() = viewModelScope.launch {
        getAddressUseCase()
            .onSuccess {
                _myAddress.value = it
                loadGroups()
            }
            .onFailure {
                _uiState.value = GroupListUiState.NotAddress
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
        val filters = groupFilterSelector.currentSelectedFilters.value ?: listOf()
        _groupListEvent.emit(
            GroupListEvent.OpenFilterSelector(
                groupFilterType = GroupFilter.SizeFilter.Init,
                groupFilters = filters,
            ),
        )
    }

    override fun selectGenderFilter() {
        val filters = groupFilterSelector.currentSelectedFilters.value ?: listOf()
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
            GroupListEvent.Navigation.NavigateToAddress
        )
    }

    companion object {
        fun factory(
            getAddressUseCase: GetAddressUseCase
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                GroupListViewModel(
                    getAddressUseCase = getAddressUseCase
                )
            }
        }

        // TODO: remove dummy
        fun dummy(): List<GroupListUiModel> {
            return List(5) {
                listOf(
                    GroupListUiModel(
                        groupId = 0L,
                        filters =
                        listOf(
                            GroupFilter.SizeFilter.SmallDog,
                            GroupFilter.GenderFilter.Female,
                            GroupFilter.GenderFilter.NeutralizingMale,
                        ),
                        groupPoster = "",
                        isParticipable = true,
                        title = "중형견 모임해요",
                        content = "공지 꼭 읽어주세요",
                        maximumNumberOfPeople = 5,
                        currentNumberOfPeople = 2,
                        groupLocation = "잠실6동",
                        groupLeader = "벼리",
                        groupDate = LocalDateTime.now(),
                        groupWoofs = listOf(),
                        groupReaderImage = "",
                    ),
                    GroupListUiModel(
                        groupId = 0L,
                        filters =
                        listOf(
                            GroupFilter.SizeFilter.SmallDog,
                            GroupFilter.GenderFilter.Female,
                        ),
                        groupPoster = "",
                        isParticipable = true,
                        title = "중형견 모임해요",
                        content = "공지 꼭 읽어주세요",
                        maximumNumberOfPeople = 5,
                        currentNumberOfPeople = 3,
                        groupLocation = "잠실5동",
                        groupLeader = "채드",
                        groupDate = LocalDateTime.of(2024, 7, 2, 14, 12, 0),
                        groupWoofs = listOf(),
                        groupReaderImage = "",
                    ),
                )
            }.flatten()
        }
    }
}
