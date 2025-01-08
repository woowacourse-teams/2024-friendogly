package com.happy.friendogly.presentation.ui.club.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.model.SearchClubPageInfo
import com.happy.friendogly.domain.model.UserAddress
import com.happy.friendogly.domain.usecase.GetAddressUseCase
import com.happy.friendogly.domain.usecase.GetPetsMineUseCase
import com.happy.friendogly.domain.usecase.GetSearchingClubsUseCase
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.club.common.ClubErrorHandler
import com.happy.friendogly.presentation.ui.club.common.ClubItemActionHandler
import com.happy.friendogly.presentation.ui.club.common.mapper.toDomain
import com.happy.friendogly.presentation.ui.club.common.mapper.toGenders
import com.happy.friendogly.presentation.ui.club.common.mapper.toPresentation
import com.happy.friendogly.presentation.ui.club.common.mapper.toSizeTypes
import com.happy.friendogly.presentation.ui.club.common.model.ClubFilterSelector
import com.happy.friendogly.presentation.ui.club.common.model.ClubItemUiModel
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ClubFilter
import com.happy.friendogly.presentation.ui.club.common.model.clubfilter.ParticipationFilter
import com.happy.friendogly.presentation.utils.logClubDetailClick
import com.happy.friendogly.presentation.utils.logListAddClubClick
import com.happy.friendogly.presentation.utils.logUpdateUserLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class ClubListViewModel
    @Inject
    constructor(
        private val analyticsHelper: AnalyticsHelper,
        private val getPetsMineUseCase: GetPetsMineUseCase,
        private val getAddressUseCase: GetAddressUseCase,
        private val searchingClubsUseCase: GetSearchingClubsUseCase,
    ) : BaseViewModel(), ClubListActionHandler, ClubItemActionHandler {
        val clubErrorHandler = ClubErrorHandler()

        private val _uiState: MutableLiveData<ClubListUiState> =
            MutableLiveData(ClubListUiState.Loading)
        val uiState: LiveData<ClubListUiState> get() = _uiState

        private val _myAddress: MutableLiveData<UserAddress> =
            MutableLiveData(null)
        val myAddress: LiveData<UserAddress> get() = _myAddress

        private val _participationFilter: MutableLiveData<ParticipationFilter> =
            MutableLiveData(ParticipationFilter.ENTIRE)
        val participationFilter: LiveData<ParticipationFilter> get() = _participationFilter

        val clubFilterSelector = ClubFilterSelector()

        private val _clubs: MutableStateFlow<PagingData<ClubItemUiModel>> =
            MutableStateFlow(PagingData.empty())
        val clubs: StateFlow<PagingData<ClubItemUiModel>> = _clubs.asStateFlow()

        private val _clubListEvent: MutableLiveData<Event<ClubListEvent>> = MutableLiveData()
        val clubListEvent: LiveData<Event<ClubListEvent>> get() = _clubListEvent

        private var searchClubPageInfo: SearchClubPageInfo = SearchClubPageInfo()

        init {
            initPetState()
            loadClubWithAddress()
        }

        fun loadClubWithAddress() =
            viewModelScope.launch {
                _uiState.value = ClubListUiState.Loading
                getAddressUseCase()
                    .onSuccess {
                        _myAddress.value = it
                        _clubListEvent.emit(ClubListEvent.ResetPaging)
                    }
                    .onFailure {
                        _uiState.value = ClubListUiState.NotAddress
                    }
            }

        private fun initPetState() =
            launch {
                getPetsMineUseCase().fold(
                    onSuccess = { pets ->
                        if (isInValidPetCount(pets)) {
                            _clubListEvent.emit(ClubListEvent.OpenAddPet)
                        }
                    },
                    onError = { error ->
                        clubErrorHandler.handle(error)
                    },
                )
            }

        fun loadClubs() =
            launch {
                val currentFilterCondition = participationFilter.value ?: return@launch
                val currentAddress = myAddress.value ?: return@launch
                searchingClubsUseCase(
                    searchClubPageInfo = searchClubPageInfo,
                    filterCondition = currentFilterCondition.toDomain(),
                    address = currentAddress.toDomain(),
                    genderParams = clubFilterSelector.selectGenderFilters().toGenders(),
                    sizeParams = clubFilterSelector.selectSizeFilters().toSizeTypes(),
                )
                    .cachedIn(viewModelScope)
                    .collectLatest { result ->

                        _clubs.value = result.toPresentation()
                    }
            }

        fun handleDomainError(throwable: Throwable) {
            if (throwable is NullPointerException) {
                _uiState.value = ClubListUiState.NotData
                return
            }

            clubErrorHandler.handle(
                when (throwable) {
                    is ConnectException, is UnknownHostException -> DataError.Network.NO_INTERNET
                    else -> DataError.Network.SERVER_ERROR
                },
            )
            _uiState.value = ClubListUiState.Error
        }

        fun updateClubFilter(filters: List<ClubFilter>) {
            clubFilterSelector.initClubFilter(filters)
            loadClubWithAddress()
        }

        fun updateParticipationFilter(participationFilter: ParticipationFilter) {
            _participationFilter.value = participationFilter
            loadClubWithAddress()
        }

        fun updateInitState() {
            _uiState.value = ClubListUiState.Init
        }

        override fun loadClub(clubId: Long) {
            analyticsHelper.logClubDetailClick()
            _clubListEvent.emit(ClubListEvent.OpenClub(clubId))
        }

        override fun addClub() {
            analyticsHelper.logListAddClubClick()
            applyAddClubState()
        }

        private fun applyAddClubState() =
            launch {
                getPetsMineUseCase().fold(
                    onSuccess = { pets ->
                        if (isInValidPetCount(pets)) {
                            _clubListEvent.emit(ClubListEvent.OpenAddPet)
                        } else if (myAddress.value == null) {
                            _clubListEvent.emit(ClubListEvent.FailLocation)
                        } else {
                            _clubListEvent.emit(ClubListEvent.Navigation.NavigateToAddClub)
                        }
                    },
                    onError = { error ->
                        clubErrorHandler.handle(error)
                    },
                )
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
            analyticsHelper.logUpdateUserLocation()
            _clubListEvent.emit(
                ClubListEvent.Navigation.NavigateToAddress,
            )
        }

        private fun isInValidPetCount(pets: List<Pet>): Boolean = pets.isEmpty()
    }
