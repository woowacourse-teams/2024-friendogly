package com.happy.friendogly.presentation.ui.playground.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.R
import com.happy.friendogly.data.mapper.toPlayground
import com.happy.friendogly.domain.usecase.DeletePlaygroundLeaveUseCase
import com.happy.friendogly.domain.usecase.GetPetExistenceUseCase
import com.happy.friendogly.domain.usecase.GetPlaygroundInfoUseCase
import com.happy.friendogly.domain.usecase.GetPlaygroundSummaryUseCase
import com.happy.friendogly.domain.usecase.GetPlaygroundsUseCase
import com.happy.friendogly.domain.usecase.PatchPlaygroundArrivalUseCase
import com.happy.friendogly.domain.usecase.PostPlaygroundJoinUseCase
import com.happy.friendogly.domain.usecase.PostPlaygroundUseCase
import com.happy.friendogly.firebase.analytics.AnalyticsHelper
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundActionHandler
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundNavigateAction
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundTrackingModeAction
import com.happy.friendogly.presentation.ui.playground.mapper.toPresentation
import com.happy.friendogly.presentation.ui.playground.model.PlayStatus
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundSummary
import com.happy.friendogly.presentation.ui.playground.state.PlaygroundUiState
import com.happy.friendogly.presentation.ui.playground.uimodel.PlaygroundInfoUiModel
import com.happy.friendogly.presentation.ui.playground.uimodel.PlaygroundMarkerUiModel
import com.happy.friendogly.presentation.ui.playground.uimodel.PlaygroundRegisterBtnUiModel
import com.happy.friendogly.presentation.ui.playground.util.ANIMATE_DURATION_MILLIS
import com.happy.friendogly.presentation.utils.logBackBtnClicked
import com.happy.friendogly.presentation.utils.logCloseBtnClicked
import com.happy.friendogly.presentation.utils.logHelpBtnClicked
import com.happy.friendogly.presentation.utils.logLocationBtnClicked
import com.happy.friendogly.presentation.utils.logMyPlaygroundBtnClicked
import com.happy.friendogly.presentation.utils.logPetExistence
import com.happy.friendogly.presentation.utils.logPetExistenceBtnClicked
import com.happy.friendogly.presentation.utils.logPetImageClicked
import com.happy.friendogly.presentation.utils.logPlaygroundMemberNameClicked
import com.happy.friendogly.presentation.utils.logPlaygroundSize
import com.happy.friendogly.presentation.utils.logRefreshBtnClicked
import com.happy.friendogly.presentation.utils.logRegisterMarkerBtnClicked
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaygroundViewModel
    @Inject
    constructor(
        private val analyticsHelper: AnalyticsHelper,
        private val postPlaygroundUseCase: PostPlaygroundUseCase,
        private val patchPlaygroundArrivalUseCase: PatchPlaygroundArrivalUseCase,
        private val getPlaygroundsUseCase: GetPlaygroundsUseCase,
        private val getPetExistenceUseCase: GetPetExistenceUseCase,
        private val getPlaygroundInfoUseCase: GetPlaygroundInfoUseCase,
        private val getPlaygroundSummaryUseCase: GetPlaygroundSummaryUseCase,
        private val postPlaygroundJoinUseCase: PostPlaygroundJoinUseCase,
        private val deletePlaygroundLeaveUseCase: DeletePlaygroundLeaveUseCase,
    ) : BaseViewModel(),
        PlaygroundActionHandler {
        private val _uiState: MutableLiveData<PlaygroundUiState> = MutableLiveData()
        val uiState: LiveData<PlaygroundUiState> get() = _uiState

        private val _myPlayStatus: MutableLiveData<PlayStatus> = MutableLiveData()
        val myPlayStatus: LiveData<PlayStatus> get() = _myPlayStatus

        private val _myPlayground: MutableLiveData<PlaygroundMarkerUiModel?> = MutableLiveData()
        val myPlayground: LiveData<PlaygroundMarkerUiModel?> get() = _myPlayground

        private val _nearPlaygrounds: MutableLiveData<List<PlaygroundMarkerUiModel>> = MutableLiveData()
        val nearPlaygrounds: LiveData<List<PlaygroundMarkerUiModel>> get() = _nearPlaygrounds

        private val _recentlyClickedPlayground: MutableLiveData<Marker> = MutableLiveData()
        val recentlyClickedPlayground: LiveData<Marker> get() = _recentlyClickedPlayground

        private val _playgroundInfo: MutableLiveData<PlaygroundInfoUiModel?> = MutableLiveData()
        val playgroundInfo: LiveData<PlaygroundInfoUiModel?> get() = _playgroundInfo

        private val _playgroundSummary: MutableLiveData<PlaygroundSummary> = MutableLiveData()
        val playgroundSummary: LiveData<PlaygroundSummary> get() = _playgroundSummary

        private val _addressLine: MutableLiveData<String> = MutableLiveData()
        val addressLine: LiveData<String> get() = _addressLine

        private val _mapActions: MutableLiveData<Event<PlaygroundMapAction>> = MutableLiveData()
        val mapActions: LiveData<Event<PlaygroundMapAction>> get() = _mapActions

        private val _changeTrackingModeActions: MutableLiveData<Event<PlaygroundTrackingModeAction>> =
            MutableLiveData()
        val changeTrackingModeActions: LiveData<Event<PlaygroundTrackingModeAction>> get() = _changeTrackingModeActions

        private val _alertActions: MutableLiveData<Event<PlaygroundAlertAction>> = MutableLiveData()
        val alertActions: LiveData<Event<PlaygroundAlertAction>> get() = _alertActions

        private val _navigateActions: MutableLiveData<Event<PlaygroundNavigateAction>> = MutableLiveData()
        val navigateActions: LiveData<Event<PlaygroundNavigateAction>> get() = _navigateActions

        private val _refreshBtnVisible: MutableLiveData<Boolean> = MutableLiveData(false)
        val refreshBtnVisible: LiveData<Boolean> get() = _refreshBtnVisible

        private val _registerPlaygroundBtn: MutableLiveData<PlaygroundRegisterBtnUiModel> =
            MutableLiveData()
        val registerPlaygroundBtn: LiveData<PlaygroundRegisterBtnUiModel> get() = _registerPlaygroundBtn

        override fun clickPetExistenceBtn() {
            analyticsHelper.logPetExistenceBtnClicked()
            runIfLocationPermissionGranted {
                checkPetExistence()
            }
        }

        override fun clickRegisterMarkerBtn() {
            analyticsHelper.logRegisterMarkerBtnClicked()
            runIfLocationPermissionGranted {
                _mapActions.emit(PlaygroundMapAction.RegisterMyPlayground)
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
                    _mapActions.emit(PlaygroundMapAction.MoveCameraCenterPosition(myPlaygroundMarker.marker.position))
                    changeTrackingModeToNoFollow()
                } else {
                    _alertActions.emit(PlaygroundAlertAction.AlertNotExistMyPlaygroundSnackbar)
                }
            }
        }

        override fun clickRefreshBtn() {
            analyticsHelper.logRefreshBtnClicked()
            updateRefreshBtnVisibility(visible = false)
            runIfLocationPermissionGranted {
                _mapActions.emit(PlaygroundMapAction.ScanNearPlaygrounds)
            }
        }

        override fun clickBackBtn() {
            analyticsHelper.logBackBtnClicked()
            updateUiState(PlaygroundUiState.FindingPlayground)
        }

        override fun clickCloseBtn() {
            analyticsHelper.logCloseBtnClicked()
            updateUiState(PlaygroundUiState.FindingPlayground)
        }

        override fun clickPlaygroundMemberName(memberId: Long) {
            analyticsHelper.logPlaygroundMemberNameClicked()
            _navigateActions.emit(PlaygroundNavigateAction.NavigateToOtherProfile(memberId))
        }

        override fun clickHelpBtn() {
            analyticsHelper.logHelpBtnClicked()
            if (uiState.value is PlaygroundUiState.RegisteringPlayground) {
                val textResId = R.string.playground_register_help
                _alertActions.emit(PlaygroundAlertAction.AlertHelpBalloon(textResId))
            }
        }

        override fun clickPetImage(petImageUrl: String) {
            analyticsHelper.logPetImageClicked()
            _navigateActions.emit(PlaygroundNavigateAction.NavigateToPetImage(petImageUrl))
        }

        override fun clickJoinPlaygroundBtn(playgroundId: Long) {
            viewModelScope.launch {
                postPlaygroundJoinUseCase(playgroundId = playgroundId)
                    .onSuccess { playgroundJoin ->
                        val nearPlaygrounds = nearPlaygrounds.value ?: return@onSuccess
                        _myPlayground.value =
                            nearPlaygrounds.first { playground ->
                                playground.id == playgroundJoin.playgroundId
                            }
                    }.onFailure {
                        // 이미 참여한 놀이터 있을 때, 나머지 에러 처리
                        _alertActions.emit(PlaygroundAlertAction.AlertAlreadyParticipatingInPlayground)
                        _alertActions.emit(PlaygroundAlertAction.AlertFailToJoinPlaygroundSnackbar)
                    }
            }
        }

        override fun clickLeavePlaygroundBtn() {
            viewModelScope.launch {
                deletePlaygroundLeaveUseCase()
                    .onSuccess {
                        _alertActions.emit(PlaygroundAlertAction.AlertLeaveMyPlaygroundSnackbar)
                        _myPlayStatus.value = PlayStatus.NO_PLAYGROUND
                        _myPlayground.value = null
                        _playgroundInfo.value = null
                    }.onFailure {
                        _alertActions.emit(PlaygroundAlertAction.AlertFailToDeleteMyFootprintSnackbar)
                    }
            }
//        _mapActions.emit(WoofMapActions.StopWalkTimeChronometer)
        }

        private fun checkPetExistence() {
            viewModelScope.launch {
                getPetExistenceUseCase()
                    .onSuccess { petExistence ->
                        analyticsHelper.logPetExistence(petExistence.isExistPet)
                        if (!petExistence.isExistPet) {
                            _alertActions.emit(PlaygroundAlertAction.AlertHasNotPetDialog)
                        } else {
                            _uiState.value = PlaygroundUiState.Loading
                            Handler(Looper.getMainLooper()).postDelayed(
                                {
                                    _uiState.value = PlaygroundUiState.RegisteringPlayground
                                },
                                ANIMATE_DURATION_MILLIS,
                            )
                        }
                    }.onFailure {
                        // 이미 참여한 놀이터 있을 때, 나머지 에러 처리
                        _alertActions.emit(PlaygroundAlertAction.AlertAlreadyParticipatingInPlayground)
                        _alertActions.emit(PlaygroundAlertAction.AlertFailToCheckPetExistence)
                    }
            }
        }

        private fun changeLocationTrackingMode() {
            val changeTrackingModeAction =
                changeTrackingModeActions.value?.value ?: PlaygroundTrackingModeAction.FollowTrackingMode
            val trackingMode =
                if (changeTrackingModeAction is PlaygroundTrackingModeAction.FollowTrackingMode) {
                    PlaygroundTrackingModeAction.FaceTrackingMode
                } else {
                    PlaygroundTrackingModeAction.FollowTrackingMode
                }
            _changeTrackingModeActions.emit(trackingMode)
        }

        private fun runIfLocationPermissionGranted(action: () -> Unit) {
            if (uiState.value !is PlaygroundUiState.LocationPermissionsNotGranted) {
                action()
            } else {
                _alertActions.emit(PlaygroundAlertAction.AlertHasNotLocationPermissionDialog)
            }
        }

        fun registerMyPlayground(latLng: LatLng) {
            if (registerPlaygroundBtn.value?.inKorea == true) {
                viewModelScope.launch {
                    postPlaygroundUseCase(latLng.latitude, latLng.longitude)
                        .onSuccess { myPlayground ->
                            _mapActions.emit(PlaygroundMapAction.MakeMyPlaygroundMarker(myPlayground = myPlayground.toPlayground()))
                            _alertActions.emit(PlaygroundAlertAction.AlertMarkerRegisteredSnackbar)
                            _uiState.value = PlaygroundUiState.FindingPlayground
                            scanNearPlaygrounds()
                        }.onFailure {
                            // 놀이터 반경 겹쳤을 때, 나머지 에러 처리
                            _alertActions.emit(PlaygroundAlertAction.AlertOverlapPlaygroundCreationSnackbar)
                            _alertActions.emit(PlaygroundAlertAction.AlertFailToRegisterPlaygroundSnackbar)
                        }
                }
            } else {
                _alertActions.emit(PlaygroundAlertAction.AlertAddressOutOfKoreaSnackbar)
            }
        }

        fun initPlaygrounds() {
            viewModelScope.launch {
                getPlaygroundsUseCase()
                    .onSuccess { playgrounds ->
                        analyticsHelper.logPlaygroundSize(playgrounds.size)
                        val nearPlaygrounds =
                            playgrounds.filter { playground -> !playground.isParticipating }
                        _mapActions.value =
                            Event(PlaygroundMapAction.MakeNearPlaygroundMarkers(nearPlaygrounds = nearPlaygrounds))

                        val myPlayground =
                            playgrounds.firstOrNull { playground -> playground.isParticipating }
                        if (myPlayground != null) {
                            _mapActions.value =
                                Event(PlaygroundMapAction.MakeMyPlaygroundMarker(myPlayground = myPlayground))
                        }
                    }.onFailure {
                        _alertActions.emit(PlaygroundAlertAction.AlertFailToLoadPlaygroundsSnackbar)
                    }
            }
        }

        fun loadMyPlayground(
            marker: Marker,
            circleOverlay: CircleOverlay,
        ) {
            val actionsValue = mapActions.value?.value
            val myPlayground =
                (actionsValue as? PlaygroundMapAction.MakeMyPlaygroundMarker)?.myPlayground ?: return

            _myPlayground.value =
                PlaygroundMarkerUiModel(
                    id = myPlayground.id,
                    marker = marker,
                    circleOverlay = circleOverlay,
                )
        }

        fun scanNearPlaygrounds() {
            _uiState.value = PlaygroundUiState.Loading
            viewModelScope.launch {
                getPlaygroundsUseCase()
                    .onSuccess { playgrounds ->
                        analyticsHelper.logPlaygroundSize(playgrounds.size)
                        val nearPlaygrounds =
                            playgrounds.filter { playground -> !playground.isParticipating }
                        _mapActions.emit(
                            PlaygroundMapAction.MakeNearPlaygroundMarkers(
                                nearPlaygrounds = nearPlaygrounds,
                            ),
                        )
                    }.onFailure {
                        _alertActions.emit(PlaygroundAlertAction.AlertFailToLoadPlaygroundsSnackbar)
                    }
            }
        }

        fun loadNearPlaygrounds(markers: List<PlaygroundMarkerUiModel>) {
            _nearPlaygrounds.value = markers
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    _uiState.value = PlaygroundUiState.FindingPlayground
                },
                ANIMATE_DURATION_MILLIS,
            )
        }

        fun loadPlaygroundInfo(id: Long) {
            viewModelScope.launch {
                getPlaygroundInfoUseCase(id)
                    .onSuccess { playgroundInfo ->
                        _playgroundInfo.value = playgroundInfo.toPresentation()
                    }.onFailure {
                        _alertActions.emit(PlaygroundAlertAction.AlertFailToLoadPlaygroundInfoSnackbar)
                    }
            }
        }

        fun loadPlaygroundSummary(id: Long) {
            viewModelScope.launch {
                getPlaygroundSummaryUseCase(id)
                    .onSuccess { playgroundSummary ->
                        _playgroundSummary.value = playgroundSummary
                    }.onFailure {
                        _alertActions.emit(PlaygroundAlertAction.AlertFailToLoadPlaygroundSummarySnackbar)
                    }
            }
        }

        fun loadRecentlyClickedPlayground(marker: Marker) {
            _recentlyClickedPlayground.value = marker
        }

        fun updateUiState(uiState: PlaygroundUiState) {
            _uiState.value = uiState
        }

        fun updatePlaygroundArrival(latLng: LatLng) {
            viewModelScope.launch {
                patchPlaygroundArrivalUseCase(
                    latLng.latitude,
                    latLng.longitude,
                ).onSuccess { playgroundArrival ->
                    _myPlayStatus.value = playgroundArrival.toPresentation()
                }.onFailure {
                    _alertActions.emit(PlaygroundAlertAction.AlertFailToUpdatePlaygroundArrival)
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
            _registerPlaygroundBtn.value = PlaygroundRegisterBtnUiModel(cameraIdle = cameraIdle)
        }

        fun updateRegisterPlaygroundBtnInKorea(inKorea: Boolean) {
            _registerPlaygroundBtn.postValue(registerPlaygroundBtn.value?.copy(inKorea = inKorea))
        }

        fun changeTrackingModeToNoFollow() {
            _changeTrackingModeActions.emit(PlaygroundTrackingModeAction.NoFollowTrackingMode)
        }
    }
