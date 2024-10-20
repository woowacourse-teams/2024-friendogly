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
import com.happy.friendogly.domain.usecase.InsertRecentPetUseCase
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
import com.happy.friendogly.presentation.ui.playground.mapper.toPresentation
import com.happy.friendogly.presentation.ui.playground.model.PlayStatus
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundPetDetail
import com.happy.friendogly.presentation.ui.playground.model.PlaygroundSummary
import com.happy.friendogly.presentation.ui.playground.state.PlaygroundUiState
import com.happy.friendogly.presentation.ui.playground.uimodel.MyPlaygroundUiModel
import com.happy.friendogly.presentation.ui.playground.uimodel.PlaygroundInfoUiModel
import com.happy.friendogly.presentation.ui.playground.uimodel.PlaygroundUiModel
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
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PathOverlay
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
        private val insertRecentPetUseCase: InsertRecentPetUseCase,
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

        private val _myPlayground: MutableLiveData<MyPlaygroundUiModel?> = MutableLiveData()
        val myPlayground: LiveData<MyPlaygroundUiModel?> get() = _myPlayground

        private val _nearPlaygrounds: MutableLiveData<List<PlaygroundUiModel>> = MutableLiveData()
        val nearPlaygrounds: LiveData<List<PlaygroundUiModel>> get() = _nearPlaygrounds

        private val _recentlyClickedPlayground: MutableLiveData<Marker> = MutableLiveData()
        val recentlyClickedPlayground: LiveData<Marker> get() = _recentlyClickedPlayground

        private val _playgroundInfo: MutableLiveData<PlaygroundInfoUiModel?> = MutableLiveData()
        val playgroundInfo: LiveData<PlaygroundInfoUiModel?> get() = _playgroundInfo

        private val _playgroundSummary: MutableLiveData<PlaygroundSummary> = MutableLiveData()
        val playgroundSummary: LiveData<PlaygroundSummary> get() = _playgroundSummary

        private val _mapAction: MutableLiveData<Event<PlaygroundMapAction>> = MutableLiveData()
        val mapAction: LiveData<Event<PlaygroundMapAction>> get() = _mapAction

        private val _alertAction: MutableLiveData<Event<PlaygroundAlertAction>> = MutableLiveData()
        val alertAction: LiveData<Event<PlaygroundAlertAction>> get() = _alertAction

        private val _navigateAction: MutableLiveData<Event<PlaygroundNavigateAction>> =
            MutableLiveData()
        val navigateAction: LiveData<Event<PlaygroundNavigateAction>> get() = _navigateAction

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
                val myPlayground = myPlayground.value
                if (myPlayground != null) {
                    _mapAction.emit(PlaygroundMapAction.MoveCameraCenterPosition(myPlayground.marker.position))
                    changeTrackingModeToNoFollow()
                } else {
                    _alertAction.emit(PlaygroundAlertAction.AlertNotExistMyPlaygroundSnackbar)
                }
            }
        }

        override fun clickRefreshBtn() {
            analyticsHelper.logRefreshBtnClicked()
            updateUiState(PlaygroundUiState.FindingPlayground())
            runIfLocationPermissionGranted {
                _mapAction.emit(PlaygroundMapAction.ScanNearPlaygrounds)
            }
        }

        override fun clickBackBtn() {
            analyticsHelper.logBackBtnClicked()
            val currentUiState = uiState.value
            if (currentUiState is PlaygroundUiState.RegisteringPlayground) {
                currentUiState.circleOverlay.map =
                    null
            }
            updateUiState(PlaygroundUiState.FindingPlayground())
            _mapAction.emit(PlaygroundMapAction.HideRegisteringPlaygroundScreen)
        }

        override fun clickCloseBtn() {
            analyticsHelper.logCloseBtnClicked()
            val currentState = uiState.value
            if (currentState is PlaygroundUiState.RegisteringPlayground) {
                currentState.circleOverlay.map = null
            }
            updateUiState(PlaygroundUiState.FindingPlayground())
            _mapAction.emit(PlaygroundMapAction.HideRegisteringPlaygroundScreen)
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
//        _uiState.value = PlaygroundUiState.Loading
            viewModelScope.launch {
                postPlaygroundJoinUseCase(playgroundId = playgroundId).fold(
                    onSuccess = {
                        updatePlaygrounds()
                    },
                    onError = { error ->
//                    _uiState.value = PlaygroundUiState.ViewingPlaygroundInfo
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
                            hideMyPlayground()
                            _uiState.value = PlaygroundUiState.RegisteringPlayground()
                            _mapAction.emit(PlaygroundMapAction.ShowRegisteringPlaygroundScreen)
                        }
                    }.onFailure {
                        _alertAction.emit(PlaygroundAlertAction.AlertFailToCheckPetExistence)
                    }
            }
        }

        private fun changeLocationTrackingMode() {
            val actionValue =
                mapAction.value?.value
                    ?: PlaygroundMapAction.FollowTrackingMode
            val trackingMode =
                if (actionValue is PlaygroundMapAction.FollowTrackingMode) {
                    PlaygroundMapAction.FaceTrackingMode
                } else {
                    PlaygroundMapAction.FollowTrackingMode
                }
            _mapAction.emit(trackingMode)
        }

        private fun runIfLocationPermissionGranted(action: () -> Unit) {
            if (uiState.value !is PlaygroundUiState.LocationPermissionsNotGranted) {
                action()
            } else {
                _alertAction.emit(PlaygroundAlertAction.AlertHasNotLocationPermissionDialog)
            }
        }

        private fun loadPlaygroundSummary(id: Long) {
            viewModelScope.launch {
                getPlaygroundSummaryUseCase(id)
                    .onSuccess { playgroundSummary ->
                        _playgroundSummary.value = playgroundSummary
                    }.onFailure {
                        _alertAction.emit(PlaygroundAlertAction.AlertFailToLoadPlaygroundSummarySnackbar)
                    }
            }
        }

        private fun clearMyPlayground() {
            hideMyPlayground()
            _myPlayStatus.value = PlayStatus.NO_PLAYGROUND
            _myPlayground.value = null
            _playgroundInfo.value = null
        }

        private fun clearNearPlaygroundMarkers() {
            val nearPlaygroundMarkers = nearPlaygrounds.value ?: return
            nearPlaygroundMarkers.forEach { playgroundMarker ->
                playgroundMarker.marker.map = null
                playgroundMarker.circleOverlay.map = null
            }
        }

        private fun insertRecentPet(playgroundPetDetails: List<PlaygroundPetDetail>) {
            viewModelScope.launch {
                playgroundPetDetails.forEach { playgroundPetDetail ->
                    insertRecentPetUseCase(
                        memberId = playgroundPetDetail.memberId,
                        petId = playgroundPetDetail.petId,
                        name = playgroundPetDetail.name,
                        imgUrl = playgroundPetDetail.imageUrl,
                        birthday = playgroundPetDetail.birthDate,
                        gender = playgroundPetDetail.gender,
                        sizeType = playgroundPetDetail.sizeType,
                    )
                }
            }
        }

        fun registerMyPlayground(latLng: LatLng) {
            val currentUiState = uiState.value
            if (currentUiState is PlaygroundUiState.RegisteringPlayground && currentUiState.playgroundRegisterBtnClickable.inKorea) {
                viewModelScope.launch {
                    postPlaygroundUseCase(latLng.latitude, latLng.longitude).fold(
                        onSuccess = { myPlayground ->
                            currentUiState.circleOverlay.map = null
                            hideMyPlayground()
                            _mapAction.value =
                                Event(PlaygroundMapAction.HideRegisteringPlaygroundScreen)
                            _mapAction.value =
                                Event(PlaygroundMapAction.MakeMyPlaygroundMarker(myPlayground = myPlayground.toPlayground()))
                            _alertAction.emit(PlaygroundAlertAction.AlertMarkerRegisteredSnackbar)
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
            pathOverlay: PathOverlay,
        ) {
            val actionsValue = mapAction.value?.value
            val playground =
                (actionsValue as? PlaygroundMapAction.MakeMyPlaygroundMarker)?.myPlayground ?: return

            _myPlayground.value =
                MyPlaygroundUiModel(
                    id = playground.id,
                    marker = marker,
                    circleOverlay = circleOverlay,
                    pathOverlay = pathOverlay,
                )
            _recentlyClickedPlayground.value = myPlayground.value?.marker
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

        fun loadNearPlaygrounds(markers: List<PlaygroundUiModel>) {
            clearNearPlaygroundMarkers()
            _nearPlaygrounds.value = markers
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    updateUiState(PlaygroundUiState.FindingPlayground())
                },
                ANIMATE_DURATION_MILLIS,
            )
        }

        fun loadPlaygroundInfo(id: Long) {
            viewModelScope.launch {
                getPlaygroundInfoUseCase(id)
                    .onSuccess { playgroundInfo ->
                        _playgroundInfo.value = playgroundInfo.toPresentation()
                        insertRecentPet(playgroundInfo.playgroundPetDetails)
                    }.onFailure {
                        _alertAction.emit(PlaygroundAlertAction.AlertFailToLoadPlaygroundInfoSnackbar)
                    }
            }
        }

        fun showMyPlayground(map: NaverMap) {
            val myPlayground = myPlayground.value ?: return
            myPlayground.marker.map = map
            myPlayground.circleOverlay.center = myPlayground.marker.position
            myPlayground.circleOverlay.map = map
            myPlayground.pathOverlay.map = map
        }

        fun hideMyPlayground() {
            val myPlayground = myPlayground.value ?: return
            myPlayground.marker.map = null
            myPlayground.circleOverlay.map = null
            myPlayground.pathOverlay.map = null
        }

        fun playgroundMessageUpdated() {
            val myPlayground = myPlayground.value ?: return
//        _uiState.value = PlaygroundUiState.Loading
            loadPlaygroundInfo(myPlayground.id)
//        _uiState.value = PlaygroundUiState.ViewingPlaygroundInfo
        }

        fun handlePlaygroundInfo(id: Long) {
            if (id == myPlayground.value?.id) {
                loadPlaygroundInfo(id = id)
                updateUiState(PlaygroundUiState.ViewingPlaygroundInfo)
            } else {
                loadPlaygroundSummary(id = id)
                updateUiState(PlaygroundUiState.ViewingPlaygroundSummary)
            }
        }

        fun loadRecentlyClickedPlayground(marker: Marker) {
            _recentlyClickedPlayground.value = marker
        }

        fun updateUiState(uiState: PlaygroundUiState) {
            _uiState.value = uiState
        }

        fun updatePathOverlay(latLng: LatLng) {
            val myPlayground = myPlayground.value ?: return
            myPlayground.pathOverlay.coords =
                listOf(
                    latLng,
                    myPlayground.marker.position,
                )
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
                                clearMyPlayground()
                                updatePlaygrounds()
                            }

                            else -> _alertAction.emit(PlaygroundAlertAction.AlertFailToUpdatePlaygroundArrival)
                        }
                    },
                )
            }
        }

        fun leavePlayground() {
//        _uiState.value = PlaygroundUiState.Loading
            viewModelScope.launch {
                deletePlaygroundLeaveUseCase()
                    .onSuccess {
                        clearMyPlayground()
                        updatePlaygrounds()
                        _alertAction.emit(PlaygroundAlertAction.AlertLeaveMyPlaygroundSnackbar)
                    }.onFailure {
                        _alertAction.emit(PlaygroundAlertAction.AlertFailToLeavePlaygroundSnackbar)
                    }
            }
        }

        fun updateRefreshBtnVisibility(refreshBtnVisible: Boolean) {
            updateUiState(PlaygroundUiState.FindingPlayground(refreshBtnVisible = refreshBtnVisible))
        }

        fun updateCameraIdle(cameraIdle: Boolean) {
            val currentUiState = uiState.value
            if (currentUiState is PlaygroundUiState.RegisteringPlayground) {
                val updatedBtnClickable =
                    currentUiState.playgroundRegisterBtnClickable.copy(cameraIdle = cameraIdle)
                updateUiState(currentUiState.copy(playgroundRegisterBtnClickable = updatedBtnClickable))
            }
        }

        fun updateAddressAndInKorea(
            address: String,
            inKorea: Boolean,
        ) {
            val currentUiState = uiState.value
            if (currentUiState is PlaygroundUiState.RegisteringPlayground) {
                val updatedBtnClickable =
                    currentUiState.playgroundRegisterBtnClickable.copy(inKorea = inKorea)
                _uiState.postValue(
                    currentUiState.copy(
                        address = address,
                        playgroundRegisterBtnClickable = updatedBtnClickable,
                    ),
                )
            }
        }

        fun changeTrackingModeToNoFollow() {
            _mapAction.emit(PlaygroundMapAction.NoFollowTrackingMode)
        }
    }
