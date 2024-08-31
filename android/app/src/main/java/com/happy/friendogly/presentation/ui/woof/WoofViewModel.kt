package com.happy.friendogly.presentation.ui.woof

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.analytics.AnalyticsHelper
import com.happy.friendogly.data.mapper.toFootprint
import com.happy.friendogly.domain.usecase.DeleteFootprintUseCase
import com.happy.friendogly.domain.usecase.GetFootprintInfoUseCase
import com.happy.friendogly.domain.usecase.GetFootprintMarkBtnInfoUseCase
import com.happy.friendogly.domain.usecase.GetNearFootprintsUseCase
import com.happy.friendogly.domain.usecase.PatchFootprintRecentWalkStatusAutoUseCase
import com.happy.friendogly.domain.usecase.PatchFootprintRecentWalkStatusManualUseCase
import com.happy.friendogly.domain.usecase.PostFootprintUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.woof.mapper.toPresentation
import com.happy.friendogly.presentation.ui.woof.model.FilterState
import com.happy.friendogly.presentation.ui.woof.model.FootprintRecentWalkStatus
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.MyFootprintMarkerUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.OtherFootprintMarkerUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.RegisterFootprintBtnUiModel
import com.happy.friendogly.presentation.utils.logFootprintMarkBtnInfo
import com.happy.friendogly.presentation.utils.logNearFootprintSize
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import kotlinx.coroutines.launch

class WoofViewModel(
    private val analyticsHelper: AnalyticsHelper,
    private val postFootprintUseCase: PostFootprintUseCase,
    private val patchFootprintRecentWalkStatusAutoUseCase: PatchFootprintRecentWalkStatusAutoUseCase,
    private val patchFootprintRecentWalkStatusManualUseCase: PatchFootprintRecentWalkStatusManualUseCase,
    private val getNearFootprintsUseCase: GetNearFootprintsUseCase,
    private val getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
    private val getFootprintInfoUseCase: GetFootprintInfoUseCase,
    private val deleteFootprintUseCase: DeleteFootprintUseCase,
) : BaseViewModel() {
    private val _uiState: MutableLiveData<WoofUiState> = MutableLiveData()
    val uiState: LiveData<WoofUiState> get() = _uiState

    private val _filterState: MutableLiveData<FilterState> = MutableLiveData(FilterState.ALL)
    val filterState: LiveData<FilterState> get() = _filterState

    private val _myWalkStatus: MutableLiveData<FootprintRecentWalkStatus?> = MutableLiveData()
    val myWalkStatus: LiveData<FootprintRecentWalkStatus?> get() = _myWalkStatus

    private val _myFootprintMarker: MutableLiveData<MyFootprintMarkerUiModel?> = MutableLiveData()
    val myFootprintMarker: LiveData<MyFootprintMarkerUiModel?> get() = _myFootprintMarker

    private val _nearFootprintMarkers: MutableLiveData<List<OtherFootprintMarkerUiModel>> =
        MutableLiveData()
    val nearFootprintMarkers: LiveData<List<OtherFootprintMarkerUiModel>> get() = _nearFootprintMarkers

    private val _footprintInfo: MutableLiveData<FootprintInfoUiModel> = MutableLiveData()
    val footprintInfo: LiveData<FootprintInfoUiModel> get() = _footprintInfo

    private val _addressLine: MutableLiveData<String> = MutableLiveData()
    val addressLine: LiveData<String> get() = _addressLine

    private val _makeMarkerActions: MutableLiveData<Event<WoofMakeMarkerActions>> =
        MutableLiveData()
    val makeMarkerActions: LiveData<Event<WoofMakeMarkerActions>> get() = _makeMarkerActions

    private val _changeTrackingModeActions: MutableLiveData<Event<WoofTrackingModeActions>> =
        MutableLiveData()
    val changeTrackingModeActions: LiveData<Event<WoofTrackingModeActions>> get() = _changeTrackingModeActions

    private val _alertActions: MutableLiveData<Event<WoofAlertActions>> = MutableLiveData()
    val alertActions: LiveData<Event<WoofAlertActions>> get() = _alertActions

    private val _refreshBtnVisible: MutableLiveData<Boolean> = MutableLiveData(false)
    val refreshBtnVisible: LiveData<Boolean> get() = _refreshBtnVisible

    private val _registerFootprintBtn: MutableLiveData<RegisterFootprintBtnUiModel> =
        MutableLiveData()
    val registerFootprintBtn: LiveData<RegisterFootprintBtnUiModel> get() = _registerFootprintBtn

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
                        WoofAlertActions.AlertMarkBtnClickBeforeTimeoutSnackbar(
                            footPrintMarkBtnInfo.remainingTime(),
                        ),
                    )
                } else {
                    _uiState.value = WoofUiState.Loading
                    Handler(Looper.getMainLooper()).postDelayed(
                        {
                            _uiState.value = WoofUiState.RegisteringFootprint
                        },
                        ANIMATE_DURATION_MILLIS,
                    )
                }
            }.onFailure {
                _alertActions.emit(WoofAlertActions.AlertFailToLoadFootprintMarkBtnInfoSnackbar)
            }
        }
    }

    fun initFootprintMarkers(latLng: LatLng) {
        viewModelScope.launch {
            getNearFootprintsUseCase(
                latLng.latitude,
                latLng.longitude,
            ).onSuccess { nearFootprints ->
                val otherFootprints = nearFootprints.filter { footprint -> !footprint.isMine }
                analyticsHelper.logNearFootprintSize(otherFootprints.size)
                _makeMarkerActions.value =
                    Event(WoofMakeMarkerActions.MakeNearFootprintMarkers(nearFootprints = otherFootprints))

                val myFootprint = nearFootprints.firstOrNull { footprint -> footprint.isMine }
                _makeMarkerActions.value =
                    Event(WoofMakeMarkerActions.MakeMyFootprintMarker(myFootprint = myFootprint))
            }.onFailure {
                _alertActions.emit(WoofAlertActions.AlertFailToLoadNearFootprintsSnackbar)
            }
        }
    }

    fun registerFootprint(latLng: LatLng) {
        if (registerFootprintBtn.value?.inKorea == true) {
            viewModelScope.launch {
                postFootprintUseCase(latLng.latitude, latLng.longitude).onSuccess { myFootprint ->
                    _makeMarkerActions.emit(WoofMakeMarkerActions.MakeMyFootprintMarker(myFootprint = myFootprint.toFootprint()))
                    _alertActions.emit(WoofAlertActions.AlertMarkerRegisteredSnackbar)
                    _uiState.value = WoofUiState.FindingFriends
                    scanNearFootprints(latLng)
                }.onFailure {
                    _alertActions.emit(WoofAlertActions.AlertFailToRegisterFootprintSnackbar)
                }
            }
        } else {
            _alertActions.emit(WoofAlertActions.AlertAddressOutOfKoreaSnackbar)
        }
    }

    fun loadMyFootprintMarker(marker: Marker) {
        val makeMarkerActionsValue = makeMarkerActions.value?.value
        val myFootprint =
            (makeMarkerActionsValue as? WoofMakeMarkerActions.MakeMyFootprintMarker)?.myFootprint
                ?: return

        _myFootprintMarker.value =
            MyFootprintMarkerUiModel(
                footprintId = myFootprint.footprintId,
                marker = marker,
            )
    }

    fun scanNearFootprints(latLng: LatLng) {
        _uiState.value = WoofUiState.Loading
        viewModelScope.launch {
            getNearFootprintsUseCase(
                latLng.latitude,
                latLng.longitude,
            ).onSuccess { nearFootprints ->
                val otherFootprints = nearFootprints.filter { footprint -> !footprint.isMine }
                analyticsHelper.logNearFootprintSize(otherFootprints.size)
                _makeMarkerActions.emit(
                    WoofMakeMarkerActions.MakeNearFootprintMarkers(
                        nearFootprints = otherFootprints,
                    ),
                )
            }.onFailure {
                _alertActions.emit(WoofAlertActions.AlertFailToLoadNearFootprintsSnackbar)
            }
        }
    }

    fun loadNearFootprintMarkers(markers: List<Marker>) {
        val makeMarkerActionsValue = makeMarkerActions.value?.value
        val nearFootprints =
            (makeMarkerActionsValue as? WoofMakeMarkerActions.MakeNearFootprintMarkers)?.nearFootprints
                ?: return
        val nearFootprintMarkers =
            nearFootprints.mapIndexed { index, footprint ->
                OtherFootprintMarkerUiModel(
                    marker = markers[index],
                    walkStatus = footprint.walkStatus,
                )
            }

        _nearFootprintMarkers.value = nearFootprintMarkers
        Handler(Looper.getMainLooper()).postDelayed(
            {
                _uiState.value = WoofUiState.FindingFriends
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
                _alertActions.emit(WoofAlertActions.AlertFailToLoadFootprintInfoSnackbar)
            }
        }
    }

    fun updateUiState(uiState: WoofUiState) {
        _uiState.value = uiState
    }

    fun updateFilterState(filterState: FilterState) {
        _filterState.value = filterState
    }

    fun updateFootprintRecentWalkStatus(latLng: LatLng) {
        viewModelScope.launch {
            patchFootprintRecentWalkStatusAutoUseCase(
                latLng.latitude,
                latLng.longitude,
            ).onSuccess { footprintRecentWalkStatus ->
                when (footprintRecentWalkStatus.walkStatus) {
                    WalkStatus.BEFORE -> {
                        if (myWalkStatus.value == null || myWalkStatus.value?.walkStatus == WalkStatus.AFTER) {
                            _myWalkStatus.value = footprintRecentWalkStatus
                        }
                    }

                    WalkStatus.ONGOING -> {
                        if (myWalkStatus.value?.walkStatus != WalkStatus.ONGOING) {
                            _myWalkStatus.value = footprintRecentWalkStatus
                        }
                    }

                    WalkStatus.AFTER ->
                        if (myWalkStatus.value == null || myWalkStatus.value?.walkStatus == WalkStatus.ONGOING) {
                            _myWalkStatus.value = footprintRecentWalkStatus
                        }
                }
            }.onFailure {
                _alertActions.emit(WoofAlertActions.AlertFailToUpdateFootprintWalkStatusSnackbar)
            }
        }
    }

    fun updateAddressLine(addressLine: String) {
        _addressLine.postValue(addressLine)
    }

    fun updateRefreshBtnVisibility(visible: Boolean) {
        _refreshBtnVisible.value = visible
    }

    fun updateRegisterFootprintBtnCameraIdle(cameraIdle: Boolean) {
        _registerFootprintBtn.value = RegisterFootprintBtnUiModel(cameraIdle = cameraIdle)
    }

    fun updateRegisterFootprintBtnInKorea(inKorea: Boolean) {
        _registerFootprintBtn.postValue(registerFootprintBtn.value?.copy(inKorea = inKorea))
    }

    fun endWalk() {
        viewModelScope.launch {
            patchFootprintRecentWalkStatusManualUseCase(walkStatus = WalkStatus.AFTER).onSuccess { footprintRecentWalkStatus ->
                _alertActions.emit(WoofAlertActions.AlertEndWalkSnackbar)
                _myWalkStatus.value = footprintRecentWalkStatus
            }.onFailure {
                _alertActions.emit(WoofAlertActions.AlertFailToEndWalkSnackbar)
            }
        }
    }

    fun deleteMyFootprintMarker() {
        viewModelScope.launch {
            val myFootprintMarker = myFootprintMarker.value ?: return@launch
            deleteFootprintUseCase(footprintId = myFootprintMarker.footprintId).onSuccess {
                _alertActions.emit(WoofAlertActions.AlertDeleteMyFootprintMarkerSnackbar)
                _myWalkStatus.value = null
                _myFootprintMarker.value = null
            }.onFailure {
                _alertActions.emit(WoofAlertActions.AlertFailToDeleteMyFootprintSnackbar)
            }
        }
    }

    fun changeLocationTrackingMode() {
        val changeTrackingModeAction =
            changeTrackingModeActions.value?.value
                ?: WoofTrackingModeActions.ToFollowTrackingMode
        val trackingMode =
            if (changeTrackingModeAction is WoofTrackingModeActions.ToFollowTrackingMode) {
                WoofTrackingModeActions.ToFaceTrackingMode
            } else {
                WoofTrackingModeActions.ToFollowTrackingMode
            }
        _changeTrackingModeActions.emit(trackingMode)
    }

    fun changeTrackingModeToNoFollow() {
        _changeTrackingModeActions.emit(WoofTrackingModeActions.ToNoFollowTrackingMode)
    }

    companion object {
        fun factory(
            analyticsHelper: AnalyticsHelper,
            postFootprintUseCase: PostFootprintUseCase,
            patchFootprintRecentWalkStatusAutoUseCase: PatchFootprintRecentWalkStatusAutoUseCase,
            patchFootprintRecentWalkStatusManualUseCase: PatchFootprintRecentWalkStatusManualUseCase,
            getNearFootprintsUseCase: GetNearFootprintsUseCase,
            getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
            getFootprintInfoUseCase: GetFootprintInfoUseCase,
            deleteFootprintUseCase: DeleteFootprintUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                WoofViewModel(
                    analyticsHelper = analyticsHelper,
                    postFootprintUseCase = postFootprintUseCase,
                    patchFootprintRecentWalkStatusAutoUseCase = patchFootprintRecentWalkStatusAutoUseCase,
                    patchFootprintRecentWalkStatusManualUseCase = patchFootprintRecentWalkStatusManualUseCase,
                    getNearFootprintsUseCase = getNearFootprintsUseCase,
                    getFootprintMarkBtnInfoUseCase = getFootprintMarkBtnInfoUseCase,
                    getFootprintInfoUseCase = getFootprintInfoUseCase,
                    deleteFootprintUseCase = deleteFootprintUseCase,
                )
            }
        }
    }
}
