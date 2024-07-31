package com.happy.friendogly.presentation.ui.woof

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.mapper.toPresentation
import com.happy.friendogly.domain.usecase.GetFootprintInfoUseCase
import com.happy.friendogly.domain.usecase.GetFootprintMarkBtnInfoUseCase
import com.happy.friendogly.domain.usecase.GetNearFootprintsUseCase
import com.happy.friendogly.domain.usecase.PostFootprintUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintMarkBtnInfoUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintUiModel
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.launch

class WoofViewModel(
    private val postFootprintUseCase: PostFootprintUseCase,
    private val getNearFootprintsUseCase: GetNearFootprintsUseCase,
    private val getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
    private val getFootprintInfoUseCase: GetFootprintInfoUseCase,
) : BaseViewModel(), WoofActionHandler {
    private val _uiState: MutableLiveData<WoofUiState> = MutableLiveData()
    val uiState: LiveData<WoofUiState> get() = _uiState

    private val _footprintInfo: MutableLiveData<FootprintInfoUiModel> = MutableLiveData()
    val footprintInfo: LiveData<FootprintInfoUiModel> get() = _footprintInfo

    private val _mapActions: MutableLiveData<Event<WoofMapActions>> = MutableLiveData()
    val mapActions: LiveData<Event<WoofMapActions>> get() = _mapActions

    private val _snackbarActions: MutableLiveData<Event<WoofSnackbarActions>> = MutableLiveData()
    val snackbarActions: LiveData<Event<WoofSnackbarActions>> get() = _snackbarActions

    fun markFootprint(latLng: LatLng) {
        viewModelScope.launch {
            getFootprintMarkBtnInfoUseCase().onSuccess { footPrintMarkBtnInfo ->
                if (!footPrintMarkBtnInfo.hasPet) {
                    _snackbarActions.emit(WoofSnackbarActions.ShowHasNotPetSnackbar)
                } else if (!footPrintMarkBtnInfo.isMarkBtnClickable()) {
                    _snackbarActions.emit(
                        WoofSnackbarActions.ShowCantClickMarkBtnSnackbar(
                            footPrintMarkBtnInfo.remainingTime(),
                        ),
                    )
                } else {
                    loadNearFootprints(latLng, footPrintMarkBtnInfo.toPresentation())
                }
            }.onFailure {
            }
        }
    }

    fun changeMapTrackingModeToNoFollow() {
        _mapActions.emit(WoofMapActions.ChangeMapToNoFollowTrackingMode)
    }

    fun loadFootPrintInfo(footprintId: Long) {
        viewModelScope.launch {
            getFootprintInfoUseCase(footprintId).onSuccess { footPrintInfo ->
                _footprintInfo.value = footPrintInfo.toPresentation()
            }.onFailure {
            }
        }
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
                        footprintSave = footprintSave,
                        footprintMarkBtnInfo = footprintMarkBtnInfo,
                        nearFootprints = nearFootprints,
                    )
            }.onFailure {
            }
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
        val mapAction = mapActions.value?.value ?: WoofMapActions.ChangeMapToFollowTrackingMode
        _mapActions.emit(
            if (mapAction is WoofMapActions.ChangeMapToFollowTrackingMode) {
                WoofMapActions.ChangeMapToFaceTrackingMode
            } else {
                WoofMapActions.ChangeMapToFollowTrackingMode
            },
        )
    }

    companion object {
        fun factory(
            postFootprintUseCase: PostFootprintUseCase,
            getNearFootprintsUseCase: GetNearFootprintsUseCase,
            getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
            getFootprintInfoUseCase: GetFootprintInfoUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                WoofViewModel(
                    postFootprintUseCase = postFootprintUseCase,
                    getNearFootprintsUseCase = getNearFootprintsUseCase,
                    getFootprintMarkBtnInfoUseCase = getFootprintMarkBtnInfoUseCase,
                    getFootprintInfoUseCase = getFootprintInfoUseCase,
                )
            }
        }
    }
}
