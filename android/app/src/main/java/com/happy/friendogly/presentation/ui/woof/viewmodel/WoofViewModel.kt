package com.happy.friendogly.presentation.ui.woof.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.R
import com.happy.friendogly.data.mapper.toPlayground
import com.happy.friendogly.domain.usecase.DeleteFootprintUseCase
import com.happy.friendogly.domain.usecase.GetPetExistenceUseCase
import com.happy.friendogly.domain.usecase.GetPlaygroundInfoUseCase
import com.happy.friendogly.domain.usecase.GetPlaygroundsUseCase
import com.happy.friendogly.domain.usecase.PatchPlaygroundArrivalUseCase
import com.happy.friendogly.domain.usecase.PostPlaygroundUseCase
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
import com.happy.friendogly.presentation.ui.woof.model.PlayStatus
import com.happy.friendogly.presentation.ui.woof.state.WoofUiState
import com.happy.friendogly.presentation.ui.woof.uimodel.MyPlaygroundMarkerUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.PlaygroundInfoUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.RegisterPlaygroundBtnUiModel
import com.happy.friendogly.presentation.ui.woof.util.ANIMATE_DURATION_MILLIS
import com.happy.friendogly.presentation.utils.logBackBtnClicked
import com.happy.friendogly.presentation.utils.logCloseBtnClicked
import com.happy.friendogly.presentation.utils.logFootprintMemberNameClicked
import com.happy.friendogly.presentation.utils.logHelpBtnClicked
import com.happy.friendogly.presentation.utils.logLocationBtnClicked
import com.happy.friendogly.presentation.utils.logMyPlaygroundBtnClicked
import com.happy.friendogly.presentation.utils.logPetExistence
import com.happy.friendogly.presentation.utils.logPetExistenceBtnClicked
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
        private val postPlaygroundUseCase: PostPlaygroundUseCase,
        private val patchPlaygroundArrivalUseCase: PatchPlaygroundArrivalUseCase,
        private val getPlaygroundsUseCase: GetPlaygroundsUseCase,
        private val getPetExistenceUseCase: GetPetExistenceUseCase,
        private val getPlaygroundInfoUseCase: GetPlaygroundInfoUseCase,
        private val deleteFootprintUseCase: DeleteFootprintUseCase,
    ) : BaseViewModel(), WoofActionHandler {
        private val _uiState: MutableLiveData<WoofUiState> = MutableLiveData()
        val uiState: LiveData<WoofUiState> get() = _uiState

        private val _myPlayStatus: MutableLiveData<PlayStatus> = MutableLiveData()
        val myPlayStatus: LiveData<PlayStatus> get() = _myPlayStatus

        private val _myPlayground: MutableLiveData<MyPlaygroundMarkerUiModel?> = MutableLiveData()
        val myPlayground: LiveData<MyPlaygroundMarkerUiModel?> get() = _myPlayground

        private val _nearPlaygrounds: MutableLiveData<List<Marker>> =
            MutableLiveData()
        val nearPlaygrounds: LiveData<List<Marker>> get() = _nearPlaygrounds

        private val _recentlyClickedPlayground: MutableLiveData<Marker> = MutableLiveData()
        val recentlyClickedPlayground: LiveData<Marker> get() = _recentlyClickedPlayground

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

        private val _registerPlaygroundBtn: MutableLiveData<RegisterPlaygroundBtnUiModel> =
            MutableLiveData()
        val registerPlaygroundBtn: LiveData<RegisterPlaygroundBtnUiModel> get() = _registerPlaygroundBtn

        fun loadPlaygroundInfo(playgroundId: Long) {
            viewModelScope.launch {
                getPlaygroundInfoUseCase(playgroundId).onSuccess { playgroundInfo ->
                    _playgroundInfo.value = playgroundInfo.toPresentation()
                }.onFailure {
                    _alertActions.emit(WoofAlertActions.AlertFailToLoadPlaygroundInfoSnackbar)
                }
            }
        }

        override fun clickPetExistenceBtn() {
            analyticsHelper.logPetExistenceBtnClicked()
            runIfLocationPermissionGranted {
                checkPetExistence()
            }
        }

        override fun clickRegisterMarkerBtn() {
            analyticsHelper.logRegisterMarkerBtnClicked()
            runIfLocationPermissionGranted {
                _mapActions.emit(WoofMapActions.RegisterMyPlayground)
            }
        }

        override fun clickLocationBtn() {
            analyticsHelper.logLocationBtnClicked()
            runIfLocationPermissionGranted {
                changeLocationTrackingMode()
            }
        }

        override fun clickMyPlaygroundBtn() {
            analyticsHelper.logMyPlaygroundBtnClicked()
            runIfLocationPermissionGranted {
                val myPlaygroundMarker = myPlayground.value
                if (myPlaygroundMarker != null) {
                    _mapActions.emit(WoofMapActions.MoveCameraCenterPosition(myPlaygroundMarker.marker.position))
                    changeTrackingModeToNoFollow()
                } else {
                    _alertActions.emit(WoofAlertActions.AlertNotExistMyPlaygroundSnackbar)
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

        override fun clickExitPlaygroundBtn() {
            exitPlayground()
//        _mapActions.emit(WoofMapActions.StopWalkTimeChronometer)
        }

        override fun clickBackBtn() {
            analyticsHelper.logBackBtnClicked()
            updateUiState(WoofUiState.FindingPlayground)
        }

        override fun clickCloseBtn() {
            analyticsHelper.logCloseBtnClicked()
            updateUiState(WoofUiState.FindingPlayground)
        }

        override fun clickFootprintMemberName(memberId: Long) {
            analyticsHelper.logFootprintMemberNameClicked()
            _navigateActions.emit(WoofNavigateActions.NavigateToOtherProfile(memberId))
        }

        override fun clickHelpBtn() {
            analyticsHelper.logHelpBtnClicked()
            // VIEWING 상태일때 help 추가
            if (uiState.value is WoofUiState.RegisteringPlayground) {
                val textResId = R.string.woof_register_playground_help
                _alertActions.emit(WoofAlertActions.AlertHelpBalloon(textResId))
            }
        }

        override fun clickPetImage(petImageUrl: String) {
            analyticsHelper.logPetImageClicked()
            _navigateActions.emit(WoofNavigateActions.NavigateToPetImage(petImageUrl))
        }

        override fun clickParticipatePlayground() {
        }

        private fun checkPetExistence() {
            viewModelScope.launch {
                getPetExistenceUseCase().onSuccess { petExistence ->
                    analyticsHelper.logPetExistence(petExistence.isExistPet)
                    if (!petExistence.isExistPet) {
                        _alertActions.emit(WoofAlertActions.AlertHasNotPetDialog)
                    } else {
                        _uiState.value = WoofUiState.Loading
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                _uiState.value = WoofUiState.RegisteringPlayground
                            },
                            ANIMATE_DURATION_MILLIS,
                        )
                    }
                }.onFailure {
                    _alertActions.emit(WoofAlertActions.AlertFailToCheckPetExistence)
                }
            }
        }

        private fun exitPlayground() {
            viewModelScope.launch {
                val myFootprintMarker = myPlayground.value ?: return@launch
                deleteFootprintUseCase(footprintId = myFootprintMarker.playgroundId).onSuccess {
                    _alertActions.emit(WoofAlertActions.AlertExitMyPlaygroundSnackbar)
                    _myPlayStatus.value = PlayStatus.NO_PLAYGROUND
                    _myPlayground.value = null
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

        fun registerMyPlayground(latLng: LatLng) {
            if (registerPlaygroundBtn.value?.inKorea == true) {
                viewModelScope.launch {
                    postPlaygroundUseCase(latLng.latitude, latLng.longitude).onSuccess { myPlayground ->
                        _mapActions.emit(WoofMapActions.MakeMyPlaygroundMarker(myPlayground = myPlayground.toPlayground()))
                        _alertActions.emit(WoofAlertActions.AlertMarkerRegisteredSnackbar)
                        _uiState.value = WoofUiState.FindingPlayground
                        scanNearPlaygrounds()
                    }.onFailure {
                        _alertActions.emit(WoofAlertActions.AlertFailToRegisterPlaygroundSnackbar)
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

        fun loadMyPlayground(marker: Marker) {
            val actionsValue = mapActions.value?.value
            val myPlayground =
                (actionsValue as? WoofMapActions.MakeMyPlaygroundMarker)?.myPlayground
                    ?: return

            _myPlayground.value =
                MyPlaygroundMarkerUiModel(
                    playgroundId = myPlayground.id,
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

        fun loadNearPlaygrounds(markers: List<Marker>) {
            _nearPlaygrounds.value = markers
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    _uiState.value = WoofUiState.FindingPlayground
                },
                ANIMATE_DURATION_MILLIS,
            )
        }

        fun loadRecentlyClickedPlayground(marker: Marker) {
            _recentlyClickedPlayground.value = marker
        }

        fun updateUiState(uiState: WoofUiState) {
            _uiState.value = uiState
        }

        fun updatePlaygroundArrival(latLng: LatLng) {
            viewModelScope.launch {
                patchPlaygroundArrivalUseCase(
                    latLng.latitude,
                    latLng.longitude,
                ).onSuccess { playgroundArrival ->
                    // 수정해야함
                    _myPlayStatus.value =
                        if (playgroundArrival.isArrived) PlayStatus.PLAYING else PlayStatus.AWAY
                }.onFailure {
                    _alertActions.emit(WoofAlertActions.AlertFailToUpdatePlaygroundArrival)
                }
            }
        }

        fun updateAddressLine(addressLine: String) {
            _addressLine.postValue(addressLine)
        }

        fun updateRefreshBtnVisibility(visible: Boolean) {
            _refreshBtnVisible.value = visible
        }

        fun updateRegisterPlaygroundBtnCameraIdle(cameraIdle: Boolean) {
            _registerPlaygroundBtn.value = RegisterPlaygroundBtnUiModel(cameraIdle = cameraIdle)
        }

        fun updateRegisterPlaygroundBtnInKorea(inKorea: Boolean) {
            _registerPlaygroundBtn.postValue(registerPlaygroundBtn.value?.copy(inKorea = inKorea))
        }

        fun changeTrackingModeToNoFollow() {
            _changeTrackingModeActions.emit(WoofTrackingModeActions.NoFollowTrackingMode)
        }
    }
