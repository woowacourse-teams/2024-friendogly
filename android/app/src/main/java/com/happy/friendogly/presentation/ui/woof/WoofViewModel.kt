package com.happy.friendogly.presentation.ui.woof

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.analytics.AnalyticsHelper
import com.happy.friendogly.data.mapper.toFootprint
import com.happy.friendogly.domain.usecase.GetFootprintInfoUseCase
import com.happy.friendogly.domain.usecase.GetFootprintMarkBtnInfoUseCase
import com.happy.friendogly.domain.usecase.GetNearFootprintsUseCase
import com.happy.friendogly.domain.usecase.PatchWalkStatusUseCase
import com.happy.friendogly.domain.usecase.PostFootprintUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.woof.mapper.toPresentation
import com.happy.friendogly.presentation.ui.woof.model.FilterState
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintMarkerUiModel
import com.happy.friendogly.presentation.utils.logFootprintMarkBtnInfo
import com.happy.friendogly.presentation.utils.logNearFootprintSize
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import kotlinx.coroutines.launch

class WoofViewModel(
    private val analyticsHelper: AnalyticsHelper,
    private val postFootprintUseCase: PostFootprintUseCase,
    private val patchWalkStatusUseCase: PatchWalkStatusUseCase,
    private val getNearFootprintsUseCase: GetNearFootprintsUseCase,
    private val getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
    private val getFootprintInfoUseCase: GetFootprintInfoUseCase,
) : BaseViewModel() {
    private val _uiState: MutableLiveData<WoofUiState> = MutableLiveData()
    val uiState: LiveData<WoofUiState> get() = _uiState

    private val _filterState: MutableLiveData<FilterState> = MutableLiveData()
    val filterState: LiveData<FilterState> get() = _filterState

    private val _myFootprintMarker: MutableLiveData<FootprintMarkerUiModel?> = MutableLiveData()
    val myFootprintMarker: LiveData<FootprintMarkerUiModel?> get() = _myFootprintMarker

    private val _nearFootprintMarkers: MutableLiveData<List<FootprintMarkerUiModel>> =
        MutableLiveData()
    val nearFootprintMarkers: LiveData<List<FootprintMarkerUiModel>> get() = _nearFootprintMarkers

    private val _footprintInfo: MutableLiveData<FootprintInfoUiModel> = MutableLiveData()
    val footprintInfo: LiveData<FootprintInfoUiModel> get() = _footprintInfo

    private val _addressLine: MutableLiveData<String> = MutableLiveData()
    val addressLine: LiveData<String> get() = _addressLine

    private val _uiActions: MutableLiveData<Event<WoofUiActions>> = MutableLiveData()
    val uiActions: LiveData<Event<WoofUiActions>> get() = _uiActions

    private val _alertActions: MutableLiveData<Event<WoofAlertActions>> = MutableLiveData()
    val alertActions: LiveData<Event<WoofAlertActions>> get() = _alertActions

    fun loadFootprintMarkBtnInfo() {
        viewModelScope.launch {
            getFootprintMarkBtnInfoUseCase().onSuccess { footPrintMarkBtnInfo ->
                analyticsHelper.logFootprintMarkBtnInfo(
                    footPrintMarkBtnInfo.hasPet,
                    footPrintMarkBtnInfo.remainingTime(),
                )
                if (!footPrintMarkBtnInfo.hasPet) {
                    _alertActions.emit(WoofAlertActions.AlertHasNotPetDialog)
                } else if (!footPrintMarkBtnInfo.isMarkBtnClickable()) {
                    _alertActions.emit(
                        WoofAlertActions.AlertCantClickMarkBtnSnackbar(
                            footPrintMarkBtnInfo.remainingTime(),
                        ),
                    )
                } else {
                    _uiState.value = WoofUiState.RegisteringFootprint
                }
            }.onFailure {
            }
        }
    }

    fun registerFootprint(latLng: LatLng) {
        viewModelScope.launch {
            postFootprintUseCase(latLng.latitude, latLng.longitude).onSuccess { myFootprint ->
                _uiActions.value =
                    Event(WoofUiActions.MyFootprintLoaded(myFootprint = myFootprint.toFootprint()))
                _alertActions.emit(WoofAlertActions.AlertMarkerRegisteredSnackbar)
                _uiState.value = WoofUiState.FindingFriends()
                loadNearFootprints(latLng)
            }.onFailure {
            }
        }
    }

    fun loadMyFootprintMarker(myFootprintMarker: FootprintMarkerUiModel) {
        _myFootprintMarker.value = myFootprintMarker
    }

    fun initFootprints(latLng: LatLng) {
        viewModelScope.launch {
            getNearFootprintsUseCase(
                latLng.latitude,
                latLng.longitude,
            ).onSuccess { nearFootprints ->
                val otherFootprints = nearFootprints.filter { footprint -> !footprint.isMine }
                analyticsHelper.logNearFootprintSize(otherFootprints.size)
                _uiActions.value = Event(WoofUiActions.NearFootprintsLoaded(otherFootprints))

                val myFootprint = nearFootprints.firstOrNull { footprint -> footprint.isMine }
                _uiActions.value = Event(WoofUiActions.MyFootprintLoaded(myFootprint = myFootprint))
                _filterState.value = FilterState.ALL
            }.onFailure {
            }
        }
    }

    fun loadNearFootprints(latLng: LatLng) {
        _uiState.value = WoofUiState.Loading
        viewModelScope.launch {
            getNearFootprintsUseCase(
                latLng.latitude,
                latLng.longitude,
            ).onSuccess { nearFootprints ->
                val otherFootprints = nearFootprints.filter { footprint -> !footprint.isMine }
                analyticsHelper.logNearFootprintSize(otherFootprints.size)
                _uiActions.value = Event(WoofUiActions.NearFootprintsLoaded(otherFootprints))
            }.onFailure {
            }
        }
    }

    fun loadNearFootprintMarkers(nearFootprintMarkers: List<FootprintMarkerUiModel>) {
        _nearFootprintMarkers.value = nearFootprintMarkers
        Handler(Looper.getMainLooper()).postDelayed(
            {
                _uiState.value = WoofUiState.FindingFriends()
            },
            ANIMATE_DURATION_MILLIS,
        )
    }

    fun loadFootprintInfo(
        footprintId: Long,
        marker: Marker,
    ) {
        viewModelScope.launch {
            getFootprintInfoUseCase(footprintId).onSuccess { footprintInfo ->
                _footprintInfo.value = footprintInfo.toPresentation(marker)
            }.onFailure {
            }
        }
    }

    fun updateUiState(uiState: WoofUiState) {
        _uiState.value = uiState
    }

    fun updateFilterState(filterState: FilterState) {
        _filterState.value = filterState
    }

    fun updateWalkStatus(latLng: LatLng) {
        viewModelScope.launch {
            patchWalkStatusUseCase(latLng.latitude, latLng.longitude).onSuccess { walkStatus ->
                when (walkStatus) {
                    WalkStatus.BEFORE -> return@onSuccess

                    WalkStatus.ONGOING -> {
                        if (myFootprintMarker.value?.walkStatus == WalkStatus.BEFORE) {
                            _myFootprintMarker.value =
                                myFootprintMarker.value?.copy(walkStatus = WalkStatus.ONGOING)
                        }
                    }

                    WalkStatus.AFTER ->
                        if (myFootprintMarker.value?.walkStatus == WalkStatus.ONGOING) {
                            _myFootprintMarker.value =
                                myFootprintMarker.value?.copy(walkStatus = WalkStatus.AFTER)
                        }
                }
            }.onFailure {
            }
        }
    }

    fun changeRefreshBtnVisibility(visible: Boolean) {
        _uiState.value = WoofUiState.FindingFriends(refreshBtnVisible = visible)
    }

    fun changeLocationTrackingMode() {
        val mapAction = uiActions.value?.value ?: WoofUiActions.ChangeToFollowTrackingMode
        _uiActions.emit(
            if (mapAction is WoofUiActions.ChangeToFollowTrackingMode) {
                WoofUiActions.ChangeToFaceTrackingMode
            } else {
                WoofUiActions.ChangeToFollowTrackingMode
            },
        )
    }

    fun changeTrackingModeToNoFollow() {
        _uiActions.emit(WoofUiActions.ChangeToNoFollowTrackingMode)
    }

    fun loadAddressLine(addressLine: String) {
        _addressLine.value = addressLine
    }

    // API 나오면 수정
    fun endWalk() {
        _myFootprintMarker.value = myFootprintMarker.value?.copy(walkStatus = WalkStatus.AFTER)
        _alertActions.emit(WoofAlertActions.AlertEndWalkSnackbar)
    }

    companion object {
        fun factory(
            analyticsHelper: AnalyticsHelper,
            postFootprintUseCase: PostFootprintUseCase,
            patchWalkStatusUseCase: PatchWalkStatusUseCase,
            getNearFootprintsUseCase: GetNearFootprintsUseCase,
            getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
            getFootprintInfoUseCase: GetFootprintInfoUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                WoofViewModel(
                    analyticsHelper = analyticsHelper,
                    postFootprintUseCase = postFootprintUseCase,
                    patchWalkStatusUseCase = patchWalkStatusUseCase,
                    getNearFootprintsUseCase = getNearFootprintsUseCase,
                    getFootprintMarkBtnInfoUseCase = getFootprintMarkBtnInfoUseCase,
                    getFootprintInfoUseCase = getFootprintInfoUseCase,
                )
            }
        }
    }
}
