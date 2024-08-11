package com.happy.friendogly.presentation.ui.club.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.model.UserAddress
import com.happy.friendogly.domain.usecase.GetAddressUseCase
import com.happy.friendogly.domain.usecase.GetSearchingClubsUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.club.common.ClubItemActionHandler
import com.happy.friendogly.presentation.ui.club.mapper.toDomain
import com.happy.friendogly.presentation.ui.club.mapper.toGenders
import com.happy.friendogly.presentation.ui.club.mapper.toPresentation
import com.happy.friendogly.presentation.ui.club.mapper.toSizeTypes
import com.happy.friendogly.presentation.ui.club.common.model.ClubFilterSelector
import com.happy.friendogly.presentation.ui.club.common.model.ClubItemUiModel
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ParticipationFilter
import kotlinx.coroutines.launch

class ClubListViewModel(
    private val getAddressUseCase: GetAddressUseCase,
    private val searchingClubsUseCase: GetSearchingClubsUseCase,
) : BaseViewModel(), ClubListActionHandler,ClubItemActionHandler {
    private val _uiState: MutableLiveData<ClubListUiState> =
        MutableLiveData(ClubListUiState.Init)
    val uiState: LiveData<ClubListUiState> get() = _uiState

    private val _myAddress: MutableLiveData<UserAddress> =
        MutableLiveData()
    val myAddress: LiveData<UserAddress> get() = _myAddress

    private val _participationFilter: MutableLiveData<ParticipationFilter> =
        MutableLiveData(ParticipationFilter.ENTIRE)
    val participationFilter: LiveData<ParticipationFilter> get() = _participationFilter

    val clubFilterSelector = ClubFilterSelector()

    private val _clubs: MutableLiveData<List<ClubItemUiModel>> = MutableLiveData()
    val clubs: LiveData<List<ClubItemUiModel>> get() = _clubs

    private val _clubListEvent: MutableLiveData<Event<ClubListEvent>> = MutableLiveData()
    val clubListEvent: LiveData<Event<ClubListEvent>> get() = _clubListEvent

    init {
        loadClubWithAddress()
    }

    fun loadClubWithAddress() =
        viewModelScope.launch {
            _uiState.value = ClubListUiState.Init
            getAddressUseCase()
                .onSuccess {
                    _myAddress.value = it
                    loadClubs()
                }
                .onFailure {
                    _uiState.value = ClubListUiState.NotAddress
                }
        }

    private fun loadClubs() =
        viewModelScope.launch {
            searchingClubsUseCase(
                filterCondition = participationFilter.value?.toDomain() ?: return@launch,
                address = myAddress.value?.toDomain() ?: return@launch,
                genderParams = clubFilterSelector.selectGenderFilters().toGenders(),
                sizeParams = clubFilterSelector.selectSizeFilters().toSizeTypes(),
            )
                .onSuccess { clubs ->
                    if (clubs.isEmpty()) {
                        _uiState.value = ClubListUiState.NotData
                    } else {
                        _uiState.value = ClubListUiState.Init
                    }
                    _clubs.value =
                        clubs.map { club ->
                            club.toPresentation()
                        }
                }
                .onFailure {
                    _uiState.value = ClubListUiState.Error
                }
        }

    fun updateClubFilter(filters: List<ClubFilter>) {
        clubFilterSelector.initClubFilter(filters)
        loadClubWithAddress()
    }

    fun updateParticipationFilter(participationFilter: ParticipationFilter) {
        _participationFilter.value = participationFilter
        loadClubWithAddress()
    }

    override fun loadClub(clubId: Long) {
        _clubListEvent.emit(ClubListEvent.OpenClub(clubId))
    }

    override fun addClub() {
        _clubListEvent.emit(ClubListEvent.Navigation.NavigateToAddClub)
    }

    override fun selectParticipationFilter() {
        val participationFilter = participationFilter.value ?: return
        _clubListEvent.emit(ClubListEvent.OpenParticipationFilter(participationFilter))
    }

    override fun selectSizeFilter() {
        val filters = clubFilterSelector.currentSelectedFilters.value ?: emptyList()
        _clubListEvent.emit(
            ClubListEvent.OpenFilterSelector(
                clubFilterType = ClubFilter.SizeFilter.Init,
                clubFilters = filters,
            ),
        )
    }

    override fun selectGenderFilter() {
        val filters = clubFilterSelector.currentSelectedFilters.value ?: emptyList()
        _clubListEvent.emit(
            ClubListEvent.OpenFilterSelector(
                clubFilterType = ClubFilter.GenderFilter.Init,
                clubFilters = filters,
            ),
        )
    }

    override fun removeFilter(clubFilter: ClubFilter) {
        clubFilterSelector.removeClubFilter(filter = clubFilter)
        loadClubWithAddress()
    }

    override fun addMyLocation() {
        _clubListEvent.emit(
            ClubListEvent.Navigation.NavigateToAddress,
        )
    }

    companion object {
        fun factory(
            getAddressUseCase: GetAddressUseCase,
            searchingClubsUseCase: GetSearchingClubsUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                ClubListViewModel(
                    getAddressUseCase = getAddressUseCase,
                    searchingClubsUseCase = searchingClubsUseCase,
                )
            }
        }
    }
}
