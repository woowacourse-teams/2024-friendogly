package com.happy.friendogly.presentation.ui.playground.viewmodel

import android.location.Location
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.R
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
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertAddressOutOfKoreaSnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertAlreadyParticipatePlaygroundSnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertAutoLeavePlaygroundSnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertFailToCheckPetExistence
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertFailToJoinPlaygroundSnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertFailToLeavePlaygroundSnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertFailToLoadPlaygroundInfoSnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertFailToLoadPlaygroundSummarySnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertFailToLoadPlaygroundsSnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertFailToRegisterPlaygroundSnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertFailToUpdatePlaygroundArrival
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertHasNotLocationPermissionDialog
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertHasNotPetDialog
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertHelpBalloon
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertLeaveMyPlaygroundSnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertNotExistMyPlaygroundSnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertOverlapPlaygroundCreationSnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundAlertAction.AlertPlaygroundRegisteredSnackbar
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction.FaceTrackingMode
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction.FollowTrackingMode
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction.HideRegisteringPlaygroundScreen
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction.MakeMyPlaygroundMarker
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction.MakeNearPlaygroundMarkers
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction.MoveCameraCenterPosition
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction.NoFollowTrackingMode
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction.RegisterMyPlayground
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction.ShowRegisteringPlaygroundScreen
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundMapAction.StartLocationService
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundNavigateAction
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundNavigateAction.NavigateToOtherProfile
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundNavigateAction.NavigateToPetImage
import com.happy.friendogly.presentation.ui.playground.action.PlaygroundNavigateAction.NavigateToPlaygroundMessage
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
                _mapAction.emit(RegisterMyPlayground)
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
                    _mapAction.value = Event(MoveCameraCenterPosition(myPlayground.marker.position))
                    changeTrackingModeToNoFollow()
                } else {
                    _alertAction.emit(AlertNotExistMyPlaygroundSnackbar)
                }
            }
        }

        override fun clickPlaygroundRefreshBtn() {
            analyticsHelper.logRefreshBtnClicked()
            runIfLocationPermissionGranted {
                updatePlaygrounds()
            }
        }

        override fun clickPlaygroundInfoRefreshBtn(playgroundId: Long) {
            loadPlaygroundInfo(playgroundId)
        }

        override fun clickBackBtn() {
            analyticsHelper.logBackBtnClicked()
            clearRegisteringPlaygroundState()
        }

        override fun clickCloseBtn() {
            analyticsHelper.logCloseBtnClicked()
            clearRegisteringPlaygroundState()
        }

        override fun clickPlaygroundPetDetail(memberId: Long) {
            analyticsHelper.logPlaygroundPetDetailClicked()
            _navigateAction.emit(NavigateToOtherProfile(memberId))
        }

        override fun clickHelpBtn() {
            analyticsHelper.logHelpBtnClicked()
            if (uiState.value is PlaygroundUiState.RegisteringPlayground) {
                val textResId = R.string.playground_register_help
                _alertAction.emit(AlertHelpBalloon(textResId))
            }
        }

        override fun clickPetImage(petImageUrl: String) {
            analyticsHelper.logPetImageClicked()
            _navigateAction.emit(NavigateToPetImage(petImageUrl))
        }

        override fun clickPlaygroundMessage(message: String) {
            _navigateAction.emit(NavigateToPlaygroundMessage(message))
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
                                    AlertAlreadyParticipatePlaygroundSnackbar,
                                )

                            else -> _alertAction.emit(AlertFailToJoinPlaygroundSnackbar)
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
                            _alertAction.emit(AlertHasNotPetDialog)
                        } else {
                            hideMyPlayground()
                            updateUiState(PlaygroundUiState.RegisteringPlayground())
                            _mapAction.emit(ShowRegisteringPlaygroundScreen)
                        }
                    }.onFailure {
                        _alertAction.emit(AlertFailToCheckPetExistence)
                    }
            }
        }

        private fun changeLocationTrackingMode() {
            val actionValue =
                mapAction.value?.value
                    ?: FollowTrackingMode
            val trackingMode =
                if (actionValue is FollowTrackingMode) {
                    FaceTrackingMode
                } else {
                    FollowTrackingMode
                }
            _mapAction.emit(trackingMode)
        }

        private fun runIfLocationPermissionGranted(action: () -> Unit) {
            if (uiState.value !is PlaygroundUiState.LocationPermissionsNotGranted) {
                action()
            } else {
                _alertAction.emit(AlertHasNotLocationPermissionDialog)
            }
        }

        private fun loadPlaygroundSummary(id: Long) {
            viewModelScope.launch {
                getPlaygroundSummaryUseCase(id)
                    .onSuccess { playgroundSummary ->
                        _playgroundSummary.value = playgroundSummary
                    }.onFailure {
                        _alertAction.emit(AlertFailToLoadPlaygroundSummarySnackbar)
                    }
            }
        }

        private fun clearPlaygrounds() {
            clearMyPlayground()
            clearNearPlaygrounds()
        }

        private fun clearMyPlayground() {
            hideMyPlayground()
            _myPlayStatus.value = PlayStatus.NO_PLAYGROUND
            _myPlayground.value = null
            _playgroundInfo.value = null
        }

        private fun clearNearPlaygrounds() {
            val nearPlaygroundMarkers = nearPlaygrounds.value ?: return
            nearPlaygroundMarkers.forEach { playgroundMarker ->
                playgroundMarker.marker.map = null
                playgroundMarker.circleOverlay.map = null
            }
        }

        private fun clearRegisteringPlaygroundState() {
            val currentUiState = uiState.value
            if (currentUiState is PlaygroundUiState.RegisteringPlayground) {
                currentUiState.circleOverlay.map = null
            }
            _mapAction.emit(HideRegisteringPlaygroundScreen)
            updateUiState(PlaygroundUiState.FindingPlayground())
        }

        private fun insertRecentPet(playgroundPetDetails: List<PlaygroundPetDetail>) {
            viewModelScope.launch {
                val otherPetDetails =
                    playgroundPetDetails.filter { playgroundPetDetail ->
                        !playgroundPetDetail.isMine
                    }

                otherPetDetails.forEach { otherPetDetail ->
                    insertRecentPetUseCase(
                        memberId = otherPetDetail.memberId,
                        petId = otherPetDetail.petId,
                        name = otherPetDetail.name,
                        imgUrl = otherPetDetail.imageUrl,
                        birthday = otherPetDetail.birthDate,
                        gender = otherPetDetail.gender,
                        sizeType = otherPetDetail.sizeType,
                    )
                }
            }
        }

        private fun loadPlaygroundInfo(id: Long) {
            viewModelScope.launch {
                getPlaygroundInfoUseCase(id)
                    .onSuccess { playgroundInfo ->
                        updateUiState(PlaygroundUiState.Loading)
                        _playgroundInfo.value = playgroundInfo.toPresentation()
                        insertRecentPet(playgroundInfo.playgroundPetDetails)
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                updateUiState(PlaygroundUiState.ViewingPlaygroundInfo)
                            },
                            ANIMATE_DURATION_MILLIS,
                        )
                    }.onFailure {
                        _alertAction.emit(AlertFailToLoadPlaygroundInfoSnackbar)
                    }
            }
        }

        private fun hideMyPlayground() {
            val myPlayground = myPlayground.value ?: return
            myPlayground.marker.map = null
            myPlayground.circleOverlay.map = null
            myPlayground.pathOverlay.map = null
        }

        private fun withinPlaygroundRange(distance: Float): Boolean {
            val myPlayground = myPlayground.value ?: return false
            val myPlayStatus = myPlayStatus.value ?: return false
            return myPlayStatus == PlayStatus.AWAY && distance <= myPlayground.circleOverlay.radius
        }

        private fun outOfPlaygroundRange(distance: Float): Boolean {
            val myPlayground = myPlayground.value ?: return false
            val myPlayStatus = myPlayStatus.value ?: return false
            return myPlayStatus == PlayStatus.PLAYING && distance > myPlayground.circleOverlay.radius
        }

        fun updatePathOverlayByLocationChange(latLng: LatLng) {
            if (myPlayStatus.value != PlayStatus.NO_PLAYGROUND &&
                uiState.value !is PlaygroundUiState.RegisteringPlayground
            ) {
                val myPlayground = myPlayground.value ?: return
                myPlayground.pathOverlay.coords =
                    listOf(
                        latLng,
                        myPlayground.marker.position,
                    )
            }
        }

        fun handleUiStateByCameraChange() {
            when (val currentState = uiState.value) {
                is PlaygroundUiState.FindingPlayground -> {
                    if (!currentState.refreshBtnVisible) {
                        updateUiState(PlaygroundUiState.FindingPlayground(refreshBtnVisible = true))
                    }
                }

                is PlaygroundUiState.RegisteringPlayground -> {
                    updateCameraIdle(cameraIdle = false)
                }

                is PlaygroundUiState.ViewingPlaygroundSummary,
                PlaygroundUiState.ViewingPlaygroundInfo,
                -> {
                    updateUiState(
                        PlaygroundUiState.FindingPlayground(
                            refreshBtnVisible = true,
                        ),
                    )
                }

                else -> return
            }
        }

        fun registerMyPlayground(latLng: LatLng) {
            val currentUiState = uiState.value
            if (currentUiState is PlaygroundUiState.RegisteringPlayground && currentUiState.playgroundRegisterBtnClickable.inKorea) {
                viewModelScope.launch {
                    postPlaygroundUseCase(latLng.latitude, latLng.longitude).fold(
                        onSuccess = {
                            currentUiState.circleOverlay.map = null
                            _mapAction.value =
                                Event(HideRegisteringPlaygroundScreen)
                            updatePlaygrounds()
                            _alertAction.emit(AlertPlaygroundRegisteredSnackbar)
                        },
                        onError = { error ->
                            when (error) {
                                DataError.Network.OVERLAP_PLAYGROUND_CREATION ->
                                    _alertAction.emit(
                                        AlertOverlapPlaygroundCreationSnackbar,
                                    )

                                DataError.Network.ALREADY_PARTICIPATE_PLAYGROUND ->
                                    _alertAction.emit(
                                        AlertAlreadyParticipatePlaygroundSnackbar,
                                    )

                                else -> _alertAction.emit(AlertFailToRegisterPlaygroundSnackbar)
                            }
                        },
                    )
                }
            } else {
                _alertAction.emit(AlertAddressOutOfKoreaSnackbar)
            }
        }

        fun updatePlaygrounds() {
            viewModelScope.launch {
                getPlaygroundsUseCase()
                    .onSuccess { playgrounds ->
                        analyticsHelper.logPlaygroundSize(playgrounds.size)
                        clearPlaygrounds()
                        updateUiState(PlaygroundUiState.Loading)
                        val myPlayground =
                            playgrounds.firstOrNull { playground -> playground.isParticipating }
                        if (myPlayground != null) {
                            _mapAction.value =
                                Event(MakeMyPlaygroundMarker(myPlayground = myPlayground))
                        }

                        val nearPlaygrounds =
                            playgrounds.filter { playground -> !playground.isParticipating }
                        _mapAction.value =
                            Event(MakeNearPlaygroundMarkers(nearPlaygrounds = nearPlaygrounds))
                    }.onFailure {
                        _alertAction.emit(AlertFailToLoadPlaygroundsSnackbar)
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
                (actionsValue as? MakeMyPlaygroundMarker)?.myPlayground ?: return
            val myPlayground =
                MyPlaygroundUiModel(
                    id = playground.id,
                    marker = marker,
                    circleOverlay = circleOverlay,
                    pathOverlay = pathOverlay,
                )
            _myPlayground.value = myPlayground
            _recentlyClickedPlayground.value = myPlayground.marker
        }

        fun loadNearPlaygrounds(markers: List<PlaygroundUiModel>) {
            _nearPlaygrounds.value = markers
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    updateUiState(PlaygroundUiState.FindingPlayground())
                },
                ANIMATE_DURATION_MILLIS,
            )
        }

        fun showMyPlayground(map: NaverMap) {
            val myPlayground = myPlayground.value ?: return
            myPlayground.marker.map = map
            myPlayground.circleOverlay.center = myPlayground.marker.position
            myPlayground.circleOverlay.map = map
            myPlayground.pathOverlay.map = map
        }

        fun updateUiStateIfViewingPlayground() {
            if (uiState.value is PlaygroundUiState.ViewingPlaygroundInfo ||
                uiState.value is PlaygroundUiState.ViewingPlaygroundSummary
            ) {
                updateUiState(PlaygroundUiState.FindingPlayground())
            }
        }

        fun playgroundMessageUpdated() {
            val myPlayground = myPlayground.value ?: return
            loadPlaygroundInfo(myPlayground.id)
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
                            _mapAction.emit(StartLocationService)
                        }
                    },
                    onError = { error ->
                        when (error) {
                            DataError.Network.NO_PARTICIPATING_PLAYGROUND -> {
                                _alertAction.emit(AlertAutoLeavePlaygroundSnackbar)
                                updatePlaygrounds()
                            }

                            else -> _alertAction.emit(AlertFailToUpdatePlaygroundArrival)
                        }
                    },
                )
            }
        }

        fun leavePlayground() {
            viewModelScope.launch {
                deletePlaygroundLeaveUseCase()
                    .onSuccess {
                        clearMyPlayground()
                        updatePlaygrounds()
                        _alertAction.emit(AlertLeaveMyPlaygroundSnackbar)
                    }.onFailure {
                        _alertAction.emit(AlertFailToLeavePlaygroundSnackbar)
                    }
            }
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

        fun monitorDistanceAndManagePlayStatus(latLng: LatLng) {
            val distanceResults = FloatArray(1)
            val myPlayground = myPlayground.value ?: return
            val position = myPlayground.marker.position
            Location.distanceBetween(
                latLng.latitude,
                latLng.longitude,
                position.latitude,
                position.longitude,
                distanceResults,
            )
            val distance = distanceResults[0]
            if (withinPlaygroundRange(distance) || outOfPlaygroundRange(distance)) {
                updatePlaygroundArrival(latLng)
                loadPlaygroundInfo(myPlayground.id)
            }
        }

        fun changeTrackingModeToNoFollow() {
            _mapAction.emit(NoFollowTrackingMode)
        }
    }
