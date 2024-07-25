package com.woowacourse.friendogly.presentation.ui.woof

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.woowacourse.friendogly.domain.mapper.toPresentation
import com.woowacourse.friendogly.domain.usecase.GetFootprintMarkBtnInfoUseCase
import com.woowacourse.friendogly.domain.usecase.GetLandMarksUseCase
import com.woowacourse.friendogly.domain.usecase.GetNearFootprintsUseCase
import com.woowacourse.friendogly.domain.usecase.PostFootprintUseCase
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.BaseViewModelFactory
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit
import com.woowacourse.friendogly.presentation.model.FootprintMarkBtnInfoUiModel
import com.woowacourse.friendogly.presentation.model.FootprintUiModel
import kotlinx.coroutines.launch

class WoofViewModel(
    private val permissionRequester: WoofPermissionRequester,
    private val postFootprintUseCase: PostFootprintUseCase,
    private val getNearFootprintsUseCase: GetNearFootprintsUseCase,
    private val getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
    private val getLandMarksUseCase: GetLandMarksUseCase,
) : BaseViewModel(), WoofActionHandler {
    private val _uiState: MutableLiveData<WoofUiState> = MutableLiveData()
    val uiState: LiveData<WoofUiState> get() = _uiState

    private val _mapActions: MutableLiveData<Event<WoofMapActions>> = MutableLiveData()
    val mapActions: LiveData<Event<WoofMapActions>> get() = _mapActions

    private val _snackbarActions: MutableLiveData<Event<WoofSnackbarActions>> = MutableLiveData()
    val snackbarActions: LiveData<Event<WoofSnackbarActions>> get() = _snackbarActions

    fun markFootprint(latLng: LatLng) {
        viewModelScope.launch {
            getFootprintMarkBtnInfoUseCase().onSuccess { footPrintMarkBtnInfo ->
                if (footPrintMarkBtnInfo.isMarkBtnClickable()) {
                    loadNearFootprints(latLng, footPrintMarkBtnInfo.toPresentation())
                } else {
                    _snackbarActions.emit(WoofSnackbarActions.ShowCantMarkSnackbar(footPrintMarkBtnInfo.remainingTime()))
                }
            }.onFailure {
            }
        }
    }

    fun changeMapTrackingModeToNoFollow() {
        _mapActions.emit(WoofMapActions.ChangeMapToNoFollowTrackingMode)
    }

    private suspend fun loadNearFootprints(
        latLng: LatLng,
        footprintMarkBtnInfo: FootprintMarkBtnInfoUiModel,
    ) {
        viewModelScope.launch {
            getNearFootprintsUseCase(
                latLng.latitude,
                latLng.longitude,
            ).onSuccess { nearFootPrints ->
                markMyFootprint(latLng, footprintMarkBtnInfo, nearFootPrints.toPresentation())
            }.onFailure {
            }
        }
    }

    private suspend fun markMyFootprint(
        latLng: LatLng,
        footprintMarkBtnInfo: FootprintMarkBtnInfoUiModel,
        nearFootprints: List<FootprintUiModel>,
    ) {
        viewModelScope.launch {
            postFootprintUseCase(latLng.latitude, latLng.longitude).onSuccess { footprintSave ->
                val state = uiState.value ?: WoofUiState()
                _uiState.value =
                    state.copy(
                        createdFootprintId = footprintSave.footprintId,
                        footprintMarkBtnInfo = footprintMarkBtnInfo,
                        nearFootprints = nearFootprints,
                    )
            }.onFailure {
            }
        }
    }

    private suspend fun loadLandMarks() {
        viewModelScope.launch {
            getLandMarksUseCase().onSuccess { landMarks ->
                val state = uiState.value ?: return@onSuccess
                _uiState.value = state.copy(landMarks = landMarks.toPresentation())
            }.onFailure {}
        }
    }

    override fun markFootPrint() {
        if (permissionRequester.hasLocationPermissions()) {
            _mapActions.emit(WoofMapActions.MarkFootPrint)
        } else {
            _snackbarActions.emit(WoofSnackbarActions.ShowSettingSnackbar)
        }
    }

    override fun changeLocationTrackingMode() {
        if (permissionRequester.hasLocationPermissions()) {
            val mapAction = mapActions.value?.value ?: WoofMapActions.ChangeMapToFollowTrackingMode
            _mapActions.emit(
                if (mapAction is WoofMapActions.ChangeMapToFollowTrackingMode) {
                    WoofMapActions.ChangeMapToFaceTrackingMode
                } else {
                    WoofMapActions.ChangeMapToFollowTrackingMode
                },
            )
        } else {
            _mapActions.emit(WoofMapActions.ChangeMapToNoFollowTrackingMode)
            _snackbarActions.emit(WoofSnackbarActions.ShowSettingSnackbar)
        }
    }

    companion object {
        fun factory(
            permissionRequester: WoofPermissionRequester,
            postFootprintUseCase: PostFootprintUseCase,
            getNearFootprintsUseCase: GetNearFootprintsUseCase,
            getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
            getLandMarksUseCase: GetLandMarksUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                WoofViewModel(
                    permissionRequester = permissionRequester,
                    postFootprintUseCase = postFootprintUseCase,
                    getNearFootprintsUseCase = getNearFootprintsUseCase,
                    getFootprintMarkBtnInfoUseCase = getFootprintMarkBtnInfoUseCase,
                    getLandMarksUseCase = getLandMarksUseCase,
                )
            }
        }
    }
}
