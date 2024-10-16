package com.happy.friendogly.presentation.ui.playground.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.R
import com.happy.friendogly.data.mapper.toPlayground
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.fold
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
import com.happy.friendogly.presentation.utils.logPlaygroundPetDetailClicked
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
        private val _uiState: MutableLiveData<PlaygroundUiState> =
            MutableLiveData(PlaygroundUiState.Loading)
        val uiState: LiveData<PlaygroundUiState> get() = _uiState

        private val _myPlayStatus: MutableLiveData<PlayStatus> =
            MutableLiveData(PlayStatus.NO_PLAYGROUND)
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

        private val _mapAction: MutableLiveData<Event<PlaygroundMapAction>> = MutableLiveData()
        val mapAction: LiveData<Event<PlaygroundMapAction>> get() = _mapAction

        private val _changeTrackingModeAction: MutableLiveData<Event<PlaygroundTrackingModeAction>> =
            MutableLiveData()
        val changeTrackingModeAction: LiveData<Event<PlaygroundTrackingModeAction>> get() = _changeTrackingModeAction

        private val _alertAction: MutableLiveData<Event<PlaygroundAlertAction>> = MutableLiveData()
        val alertAction: LiveData<Event<PlaygroundAlertAction>> get() = _alertAction

        private val _navigateAction: MutableLiveData<Event<PlaygroundNavigateAction>> =
            MutableLiveData()
        val navigateAction: LiveData<Event<PlaygroundNavigateAction>> get() = _navigateAction

        private val _refreshBtnVisible: MutableLiveData<Boolean> = MutableLiveData(false)
        val refreshBtnVisible: LiveData<Boolean> get() = _refreshBtnVisible

        private val _playgroundRegisterBtn: MutableLiveData<PlaygroundRegisterBtnUiModel> =
            MutableLiveData()
        val playgroundRegisterBtn: LiveData<PlaygroundRegisterBtnUiModel> get() = _playgroundRegisterBtn

        override fun clickPetExistenceBtn() {
            analyticsHelper.logPetExistenceBtnClicked()
            runIfLocationPermissionGranted {
                checkPetExistence()
            }
        }

        override fun clickRegisterMarkerBtn() {
            analyticsHelper.logRegisterMarkerBtnClicked()
            runIfLocationPermissionGranted {
                _mapAction.emit(PlaygroundMapAction.RegisterMyPlayground)
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
                    _mapAction.emit(PlaygroundMapAction.MoveCameraCenterPosition(myPlaygroundMarker.marker.position))
                    changeTrackingModeToNoFollow()
                } else {
                    _alertAction.emit(PlaygroundAlertAction.AlertNotExistMyPlaygroundSnackbar)
                }
            }
        }

        override fun clickRefreshBtn() {
            analyticsHelper.logRefreshBtnClicked()
            updateRefreshBtnVisibility(visible = false)
            runIfLocationPermissionGranted {
                _mapAction.emit(PlaygroundMapAction.ScanNearPlaygrounds)
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

        override fun clickPlaygroundPetDetail(memberId: Long) {
            analyticsHelper.logPlaygroundPetDetailClicked()
            _navigateAction.emit(PlaygroundNavigateAction.NavigateToOtherProfile(memberId))
        }

        override fun clickHelpBtn() {
            analyticsHelper.logHelpBtnClicked()
            if (uiState.value is PlaygroundUiState.RegisteringPlayground) {
                val textResId = R.string.playground_register_help
                _alertAction.emit(PlaygroundAlertAction.AlertHelpBalloon(textResId))
            }
        }

        override fun clickPetImage(petImageUrl: String) {
            analyticsHelper.logPetImageClicked()
            _navigateAction.emit(PlaygroundNavigateAction.NavigateToPetImage(petImageUrl))
        }

        override fun clickPlaygroundMessage(message: String) {
            _navigateAction.emit(PlaygroundNavigateAction.NavigateToPlaygroundMessage(message))
        }

        override fun clickJoinPlaygroundBtn(playgroundId: Long) {
            viewModelScope.launch {
                postPlaygroundJoinUseCase(playgroundId = playgroundId).fold(
                    onSuccess = {
                        updatePlaygrounds()
                    },
                    onError = { error ->
                        when (error) {
                            DataError.Network.ALREADY_PARTICIPATE_PLAYGROUND ->
                                _alertAction.emit(
                                    PlaygroundAlertAction.AlertAlreadyParticipatePlaygroundSnackbar,
                                )

                            else -> _alertAction.emit(PlaygroundAlertAction.AlertFailToJoinPlaygroundSnackbar)
                        }
                    },
                )
            }
        }

        override fun clickLeavePlaygroundBtn() {
            leavePlayground()
        }

        private fun checkPetExistence() {
            viewModelScope.launch {
                getPetExistenceUseCase()
                    .onSuccess { petExistence ->
                        analyticsHelper.logPetExistence(petExistence.isExistPet)
                        if (!petExistence.isExistPet) {
                            _alertAction.emit(PlaygroundAlertAction.AlertHasNotPetDialog)
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
                        _alertAction.emit(PlaygroundAlertAction.AlertAlreadyParticipatePlaygroundSnackbar)
                        _alertAction.emit(PlaygroundAlertAction.AlertFailToCheckPetExistence)
                    }
            }
        }

        private fun changeLocationTrackingMode() {
            val changeTrackingModeAction =
                changeTrackingModeAction.value?.value
                    ?: PlaygroundTrackingModeAction.FollowTrackingMode
            val trackingMode =
                if (changeTrackingModeAction is PlaygroundTrackingModeAction.FollowTrackingMode) {
                    PlaygroundTrackingModeAction.FaceTrackingMode
                } else {
                    PlaygroundTrackingModeAction.FollowTrackingMode
                }
            _changeTrackingModeAction.emit(trackingMode)
        }

        private fun runIfLocationPermissionGranted(action: () -> Unit) {
            if (uiState.value !is PlaygroundUiState.LocationPermissionsNotGranted) {
                action()
            } else {
                _alertAction.emit(PlaygroundAlertAction.AlertHasNotLocationPermissionDialog)
            }
        }

        fun registerMyPlayground(latLng: LatLng) {
            if (playgroundRegisterBtn.value?.inKorea == true) {
                viewModelScope.launch {
                    postPlaygroundUseCase(latLng.latitude, latLng.longitude).fold(
                        onSuccess = { myPlayground ->
                            _mapAction.emit(PlaygroundMapAction.MakeMyPlaygroundMarker(myPlayground = myPlayground.toPlayground()))
                            _alertAction.emit(PlaygroundAlertAction.AlertMarkerRegisteredSnackbar)
                            _uiState.value = PlaygroundUiState.FindingPlayground
                            scanNearPlaygrounds()
                        },
                        onError = { error ->
                            when (error) {
                                DataError.Network.OVERLAP_PLAYGROUND_CREATION ->
                                    _alertAction.emit(
                                        PlaygroundAlertAction.AlertOverlapPlaygroundCreationSnackbar,
                                    )

                                DataError.Network.ALREADY_PARTICIPATE_PLAYGROUND ->
                                    _alertAction.emit(
                                        PlaygroundAlertAction.AlertAlreadyParticipatePlaygroundSnackbar,
                                    )

                                else -> _alertAction.emit(PlaygroundAlertAction.AlertFailToRegisterPlaygroundSnackbar)
                            }
                        },
                    )
                }
            } else {
                _alertAction.emit(PlaygroundAlertAction.AlertAddressOutOfKoreaSnackbar)
            }
        }

        fun updatePlaygrounds() {
            viewModelScope.launch {
                getPlaygroundsUseCase()
                    .onSuccess { playgrounds ->
                        analyticsHelper.logPlaygroundSize(playgrounds.size)
                        val nearPlaygrounds =
                            playgrounds.filter { playground -> !playground.isParticipating }
                        _mapAction.value =
                            Event(PlaygroundMapAction.MakeNearPlaygroundMarkers(nearPlaygrounds = nearPlaygrounds))

                        val myPlayground =
                            playgrounds.firstOrNull { playground -> playground.isParticipating }
                        if (myPlayground != null) {
                            _mapAction.value =
                                Event(PlaygroundMapAction.MakeMyPlaygroundMarker(myPlayground = myPlayground))
                        }
                    }.onFailure {
                        _alertAction.emit(PlaygroundAlertAction.AlertFailToLoadPlaygroundsSnackbar)
                    }
            }
        }

        fun loadMyPlayground(
            marker: Marker,
            circleOverlay: CircleOverlay,
        ) {
            val actionsValue = mapAction.value?.value
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
                        _mapAction.emit(
                            PlaygroundMapAction.MakeNearPlaygroundMarkers(
                                nearPlaygrounds = nearPlaygrounds,
                            ),
                        )
                    }.onFailure {
                        _alertAction.emit(PlaygroundAlertAction.AlertFailToLoadPlaygroundsSnackbar)
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
                        _alertAction.emit(PlaygroundAlertAction.AlertFailToLoadPlaygroundInfoSnackbar)
                    }
            }
        }

        fun loadPlaygroundSummary(id: Long) {
            viewModelScope.launch {
                getPlaygroundSummaryUseCase(id)
                    .onSuccess { playgroundSummary ->
                        _playgroundSummary.value = playgroundSummary
                    }.onFailure {
                        _alertAction.emit(PlaygroundAlertAction.AlertFailToLoadPlaygroundSummarySnackbar)
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
                ).fold(
                    onSuccess = { playgroundArrival ->
                        val previousPlayStatus = myPlayStatus.value
                        val currentPlayStatus = playgroundArrival.toPresentation()
                        _myPlayStatus.value = currentPlayStatus

                        if (previousPlayStatus != currentPlayStatus) {
                            val myPlayground = myPlayground.value ?: return@launch
                            loadPlaygroundInfo(myPlayground.id)
                        }

                        if (previousPlayStatus == PlayStatus.NO_PLAYGROUND) {
                            _mapAction.emit(PlaygroundMapAction.StartLocationService)
                        }
                    },
                    onError = { error ->
                        when (error) {
                            DataError.Network.NO_PARTICIPATING_PLAYGROUND -> {
                                _alertAction.emit(PlaygroundAlertAction.AlertAutoLeavePlaygroundSnackbar)
                                _myPlayStatus.value = PlayStatus.NO_PLAYGROUND
                                _myPlayground.value = null
                                _playgroundInfo.value = null
                            }

                            else -> _alertAction.emit(PlaygroundAlertAction.AlertFailToUpdatePlaygroundArrival)
                        }
                    },
                )
            }
        }

        fun leavePlayground() {
            viewModelScope.launch {
                deletePlaygroundLeaveUseCase()
                    .onSuccess {
                        _myPlayStatus.value = PlayStatus.NO_PLAYGROUND
                        _myPlayground.value = null
                        _playgroundInfo.value = null
                        _alertAction.emit(PlaygroundAlertAction.AlertLeaveMyPlaygroundSnackbar)
                    }.onFailure {
                        _alertAction.emit(PlaygroundAlertAction.AlertFailToDeleteMyFootprintSnackbar)
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
            _playgroundRegisterBtn.value = PlaygroundRegisterBtnUiModel(cameraIdle = cameraIdle)
        }

        fun updateRegisterPlaygroundBtnInKorea(inKorea: Boolean) {
            _playgroundRegisterBtn.postValue(playgroundRegisterBtn.value?.copy(inKorea = inKorea))
        }

        fun changeTrackingModeToNoFollow() {
            _changeTrackingModeAction.emit(PlaygroundTrackingModeAction.NoFollowTrackingMode)
        }
    }
