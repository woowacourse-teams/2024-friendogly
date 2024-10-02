package com.happy.friendogly.presentation.ui.woof.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.R
import com.happy.friendogly.data.mapper.toPlayground
import com.happy.friendogly.domain.usecase.DeleteFootprintUseCase
import com.happy.friendogly.domain.usecase.GetFootprintMarkBtnInfoUseCase
import com.happy.friendogly.domain.usecase.GetPlaygroundInfoUseCase
import com.happy.friendogly.domain.usecase.GetPlaygroundsUseCase
import com.happy.friendogly.domain.usecase.PatchFootprintRecentWalkStatusAutoUseCase
import com.happy.friendogly.domain.usecase.PatchFootprintRecentWalkStatusManualUseCase
import com.happy.friendogly.domain.usecase.PostFootprintUseCase
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.woof.action.WoofActionHandler
import com.happy.friendogly.presentation.ui.woof.action.WoofAlertActions
import com.happy.friendogly.presentation.ui.woof.action.WoofMapActions
import com.happy.friendogly.presentation.ui.woof.action.WoofNavigateActions
import com.happy.friendogly.presentation.ui.woof.action.WoofTrackingModeActions
import com.happy.friendogly.presentation.ui.woof.mapper.toPresentation
import com.happy.friendogly.presentation.ui.woof.model.FootprintRecentWalkStatus
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import com.happy.friendogly.presentation.ui.woof.state.WoofUiState
import com.happy.friendogly.presentation.ui.woof.uimodel.MyFootprintMarkerUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.PlaygroundInfoUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.RegisterFootprintBtnUiModel
import com.happy.friendogly.presentation.ui.woof.util.ANIMATE_DURATION_MILLIS
import com.happy.friendogly.presentation.utils.logBackBtnClicked
import com.happy.friendogly.presentation.utils.logCloseBtnClicked
import com.happy.friendogly.presentation.utils.logFootprintMarkBtnInfo
import com.happy.friendogly.presentation.utils.logFootprintMemberNameClicked
import com.happy.friendogly.presentation.utils.logHelpBtnClicked
import com.happy.friendogly.presentation.utils.logLocationBtnClicked
import com.happy.friendogly.presentation.utils.logMarkBtnClicked
import com.happy.friendogly.presentation.utils.logMyFootprintBtnClicked
import com.happy.friendogly.presentation.utils.logPetImageClicked
import com.happy.friendogly.presentation.utils.logPlaygroundSize
import com.happy.friendogly.presentation.utils.logRefreshBtnClicked
import com.happy.friendogly.presentation.utils.logRegisterMarkerBtnClicked
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WoofViewModel
    @Inject
    constructor(
        private val analyticsHelper: AnalyticsHelper,
        private val postFootprintUseCase: PostFootprintUseCase,
        private val patchFootprintRecentWalkStatusAutoUseCase: PatchFootprintRecentWalkStatusAutoUseCase,
        private val patchFootprintRecentWalkStatusManualUseCase: PatchFootprintRecentWalkStatusManualUseCase,
        private val getPlaygroundsUseCase: GetPlaygroundsUseCase,
        private val getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
        private val getPlaygroundInfoUseCase: GetPlaygroundInfoUseCase,
        private val deleteFootprintUseCase: DeleteFootprintUseCase,
    ) : BaseViewModel(), WoofActionHandler {
        private val _uiState: MutableLiveData<WoofUiState> = MutableLiveData()
        val uiState: LiveData<WoofUiState> get() = _uiState

        private val _myWalkStatus: MutableLiveData<FootprintRecentWalkStatus?> = MutableLiveData()
        val myWalkStatus: LiveData<FootprintRecentWalkStatus?> get() = _myWalkStatus

        private val _myFootprintMarker: MutableLiveData<MyFootprintMarkerUiModel?> = MutableLiveData()
        val myFootprintMarker: LiveData<MyFootprintMarkerUiModel?> get() = _myFootprintMarker

        private val _nearPlaygroundMarkers: MutableLiveData<List<Marker>> =
            MutableLiveData()
        val nearPlaygroundMarkers: LiveData<List<Marker>> get() = _nearPlaygroundMarkers

        private val _recentlyClickedMarker: MutableLiveData<Marker> = MutableLiveData()
        val recentlyClickedMarker: LiveData<Marker> get() = _recentlyClickedMarker

        private val _playgroundInfo: MutableLiveData<PlaygroundInfoUiModel> = MutableLiveData()
        val playgroundInfo: LiveData<PlaygroundInfoUiModel> get() = _playgroundInfo

        private val _addressLine: MutableLiveData<String> = MutableLiveData()
        val addressLine: LiveData<String> get() = _addressLine

        private val _mapActions: MutableLiveData<Event<WoofMapActions>> =
            MutableLiveData()
        val mapActions: LiveData<Event<WoofMapActions>> get() = _mapActions

        private val _changeTrackingModeActions: MutableLiveData<Event<WoofTrackingModeActions>> =
            MutableLiveData()
        val changeTrackingModeActions: LiveData<Event<WoofTrackingModeActions>> get() = _changeTrackingModeActions

        private val _alertActions: MutableLiveData<Event<WoofAlertActions>> = MutableLiveData()
        val alertActions: LiveData<Event<WoofAlertActions>> get() = _alertActions

        private val _navigateActions: MutableLiveData<Event<WoofNavigateActions>> = MutableLiveData()
        val navigateActions: LiveData<Event<WoofNavigateActions>> get() = _navigateActions

        private val _refreshBtnVisible: MutableLiveData<Boolean> = MutableLiveData(false)
        val refreshBtnVisible: LiveData<Boolean> get() = _refreshBtnVisible

        private val _registerFootprintBtn: MutableLiveData<RegisterFootprintBtnUiModel> =
            MutableLiveData()
        val registerFootprintBtn: LiveData<RegisterFootprintBtnUiModel> get() = _registerFootprintBtn

        fun loadPlaygroundInfo(playgroundId: Long) {
            viewModelScope.launch {
                getPlaygroundInfoUseCase(playgroundId).onSuccess { playgroundInfo ->
                    _playgroundInfo.value = playgroundInfo.toPresentation()
                }.onFailure {
                    _alertActions.emit(WoofAlertActions.AlertFailToLoadPlaygroundInfoSnackbar)
                }
            }
        }

        override fun clickMarkBtn() {
            analyticsHelper.logMarkBtnClicked()
            runIfLocationPermissionGranted {
                loadFootprintMarkBtnInfo()
            }
        }

        override fun clickRegisterMarkerBtn() {
            analyticsHelper.logRegisterMarkerBtnClicked()
            runIfLocationPermissionGranted {
                _mapActions.emit(WoofMapActions.RegisterMyFootprint)
            }
        }

        override fun clickLocationBtn() {
            analyticsHelper.logLocationBtnClicked()
            runIfLocationPermissionGranted {
                changeLocationTrackingMode()
            }
        }

        override fun clickMyFootprintBtn() {
            analyticsHelper.logMyFootprintBtnClicked()
            runIfLocationPermissionGranted {
                val myFootprintMarker = myFootprintMarker.value
                if (myFootprintMarker != null) {
                    _mapActions.emit(WoofMapActions.MoveCameraCenterPosition(myFootprintMarker.marker.position))
                    changeTrackingModeToNoFollow()
                } else {
                    _alertActions.emit(WoofAlertActions.AlertNotExistMyFootprintSnackbar)
                }
            }
        }

        override fun clickRefreshBtn() {
            analyticsHelper.logRefreshBtnClicked()
            updateRefreshBtnVisibility(visible = false)
            runIfLocationPermissionGranted {
                _mapActions.emit(WoofMapActions.ScanNearPlaygrounds)
            }
        }

        override fun clickEndWalkBtn() {
            endWalk()
//        _mapActions.emit(WoofMapActions.StopWalkTimeChronometer)
        }

        override fun clickBackBtn() {
            analyticsHelper.logBackBtnClicked()
            updateUiState(WoofUiState.FindingFriends)
        }

        override fun clickCloseBtn() {
            analyticsHelper.logCloseBtnClicked()
            updateUiState(WoofUiState.FindingFriends)
        }

        override fun clickFootprintMemberName(memberId: Long) {
            analyticsHelper.logFootprintMemberNameClicked()
            _navigateActions.emit(WoofNavigateActions.NavigateToOtherProfile(memberId))
        }

        override fun clickHelpBtn() {
            analyticsHelper.logHelpBtnClicked()
            val textResId =
                if (uiState.value is WoofUiState.RegisteringFootprint) {
                    R.string.woof_register_playground_help
                } else {
                    when (myWalkStatus.value?.walkStatus) {
                        WalkStatus.BEFORE -> {
                            R.string.woof_walk_before_help
                        }

                        WalkStatus.ONGOING -> {
                            R.string.woof_walk_ongoing_help
                        }

                        else -> {
                            return
                        }
                    }
                }
            _alertActions.emit(WoofAlertActions.AlertHelpBalloon(textResId))
        }

        override fun clickPetImage(petImageUrl: String) {
            analyticsHelper.logPetImageClicked()
            _navigateActions.emit(WoofNavigateActions.NavigateToPetImage(petImageUrl))
        }

        override fun clickParticipatePlayground() {
        }

        private fun loadFootprintMarkBtnInfo() {
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

        private fun endWalk() {
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

//        viewModelScope.launch {
//            patchFootprintRecentWalkStatusManualUseCase(walkStatus = WalkStatus.AFTER).onSuccess { footprintRecentWalkStatus ->
//                _alertActions.emit(WoofAlertActions.AlertEndWalkSnackbar)
//                _myWalkStatus.value = footprintRecentWalkStatus
//                _myFootprintMarker.value = null
//            }.onFailure {
//                _alertActions.emit(WoofAlertActions.AlertFailToEndWalkSnackbar)
//            }
//        }
        }

        private fun changeLocationTrackingMode() {
            val changeTrackingModeAction =
                changeTrackingModeActions.value?.value
                    ?: WoofTrackingModeActions.FollowTrackingMode
            val trackingMode =
                if (changeTrackingModeAction is WoofTrackingModeActions.FollowTrackingMode) {
                    WoofTrackingModeActions.FaceTrackingMode
                } else {
                    WoofTrackingModeActions.FollowTrackingMode
                }
            _changeTrackingModeActions.emit(trackingMode)
        }

        private fun runIfLocationPermissionGranted(action: () -> Unit) {
            if (uiState.value !is WoofUiState.LocationPermissionsNotGranted) {
                action()
            } else {
                _alertActions.emit(WoofAlertActions.AlertHasNotLocationPermissionDialog)
            }
        }

        fun registerMyFootprint(latLng: LatLng) {
            if (registerFootprintBtn.value?.inKorea == true) {
                viewModelScope.launch {
                    postFootprintUseCase(latLng.latitude, latLng.longitude).onSuccess { myFootprint ->
                        _mapActions.emit(WoofMapActions.MakeMyPlaygroundMarker(myPlayground = myFootprint.toPlayground()))
                        _alertActions.emit(WoofAlertActions.AlertMarkerRegisteredSnackbar)
                        _uiState.value = WoofUiState.FindingFriends
                        scanNearPlaygrounds()
                    }.onFailure {
                        _alertActions.emit(WoofAlertActions.AlertFailToRegisterFootprintSnackbar)
                    }
                }
            } else {
                _alertActions.emit(WoofAlertActions.AlertAddressOutOfKoreaSnackbar)
            }
        }

        fun initPlaygrounds() {
            viewModelScope.launch {
                getPlaygroundsUseCase().onSuccess { playgrounds ->
                    analyticsHelper.logPlaygroundSize(playgrounds.size)
                    val nearPlaygrounds =
                        playgrounds.filter { playground -> !playground.isParticipating }
                    _mapActions.value =
                        Event(WoofMapActions.MakeNearPlaygroundMarkers(nearPlaygrounds = nearPlaygrounds))

                    val myPlayground =
                        playgrounds.firstOrNull { playground -> playground.isParticipating }
                    _mapActions.value =
                        Event(WoofMapActions.MakeMyPlaygroundMarker(myPlayground = myPlayground))
                }.onFailure {
                    _alertActions.emit(WoofAlertActions.AlertFailToLoadPlaygroundsSnackbar)
                }
            }
        }

        fun loadMyFootprintMarker(marker: Marker) {
            val makeMarkerActionsValue = mapActions.value?.value
            val myFootprint =
                (makeMarkerActionsValue as? WoofMapActions.MakeMyPlaygroundMarker)?.myPlayground
                    ?: return

            _myFootprintMarker.value =
                MyFootprintMarkerUiModel(
                    footprintId = myFootprint.id,
                    marker = marker,
                )
        }

        fun scanNearPlaygrounds() {
            _uiState.value = WoofUiState.Loading
            viewModelScope.launch {
                getPlaygroundsUseCase().onSuccess { playgrounds ->
                    analyticsHelper.logPlaygroundSize(playgrounds.size)
                    val nearPlaygrounds =
                        playgrounds.filter { playground -> !playground.isParticipating }
                    _mapActions.emit(
                        WoofMapActions.MakeNearPlaygroundMarkers(
                            nearPlaygrounds = nearPlaygrounds,
                        ),
                    )
                }.onFailure {
                    _alertActions.emit(WoofAlertActions.AlertFailToLoadPlaygroundsSnackbar)
                }
            }
        }

        fun loadNearPlaygroundMarkers(markers: List<Marker>) {
            _nearPlaygroundMarkers.value = markers
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    _uiState.value = WoofUiState.FindingFriends
                },
                ANIMATE_DURATION_MILLIS,
            )
        }

        fun loadRecentlyClickedMarker(marker: Marker) {
            _recentlyClickedMarker.value = marker
        }

        fun updateUiState(uiState: WoofUiState) {
            _uiState.value = uiState
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

        fun changeTrackingModeToNoFollow() {
            _changeTrackingModeActions.emit(WoofTrackingModeActions.NoFollowTrackingMode)
        }
    }
