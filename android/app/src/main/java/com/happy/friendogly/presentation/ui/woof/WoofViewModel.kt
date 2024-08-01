package com.happy.friendogly.presentation.ui.woof

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.GetFootprintInfoUseCase
import com.happy.friendogly.domain.usecase.GetFootprintMarkBtnInfoUseCase
import com.happy.friendogly.domain.usecase.GetNearFootprintsUseCase
import com.happy.friendogly.domain.usecase.PostFootprintUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.woof.model.Footprint
import com.happy.friendogly.presentation.ui.woof.model.FootprintInfo
import com.happy.friendogly.presentation.ui.woof.model.FootprintSave
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.launch

class WoofViewModel(
    private val postFootprintUseCase: PostFootprintUseCase,
    private val getNearFootprintsUseCase: GetNearFootprintsUseCase,
    private val getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
    private val getFootprintInfoUseCase: GetFootprintInfoUseCase,
) : BaseViewModel() {
    private val _nearFootprints: MutableLiveData<List<Footprint>> = MutableLiveData()
    val nearFootprints: LiveData<List<Footprint>> get() = _nearFootprints

    private val _footprintSave: MutableLiveData<FootprintSave> = MutableLiveData()
    val footprintSave: LiveData<FootprintSave> get() = _footprintSave

    private val _footprintInfo: MutableLiveData<FootprintInfo> = MutableLiveData()
    val footprintInfo: LiveData<FootprintInfo> get() = _footprintInfo

    private val _mapActions: MutableLiveData<Event<WoofMapActions>> = MutableLiveData()
    val mapActions: LiveData<Event<WoofMapActions>> get() = _mapActions

    private val _snackbarActions: MutableLiveData<Event<WoofSnackbarActions>> = MutableLiveData()
    val snackbarActions: LiveData<Event<WoofSnackbarActions>> get() = _snackbarActions

    fun loadNearFootprints(latLng: LatLng) {
        viewModelScope.launch {
            getNearFootprintsUseCase(
                latLng.latitude,
                latLng.longitude,
            ).onSuccess { nearFootprints ->
                _nearFootprints.value = nearFootprints
            }.onFailure {
            }
        }
    }

    fun loadFootprintInfo(footprintId: Long) {
        viewModelScope.launch {
            getFootprintInfoUseCase(footprintId).onSuccess { footPrintInfo ->
                _footprintInfo.value = footPrintInfo
            }.onFailure {
            }
        }
    }

    fun loadFootprintMarkBtnInfo(latLng: LatLng) {
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
                    markFootprint(latLng)
                }
            }.onFailure {
            }
        }
    }

    fun changeLocationTrackingMode() {
        val mapAction = mapActions.value?.value ?: WoofMapActions.ChangeMapToFollowTrackingMode
        _mapActions.emit(
            if (mapAction is WoofMapActions.ChangeMapToFollowTrackingMode) {
                WoofMapActions.ChangeMapToFaceTrackingMode
            } else {
                WoofMapActions.ChangeMapToFollowTrackingMode
            },
        )
    }

    fun changeTrackingModeToNoFollow() {
        _mapActions.emit(WoofMapActions.ChangeMapToNoFollowTrackingMode)
    }

    private suspend fun markFootprint(latLng: LatLng) {
        viewModelScope.launch {
            postFootprintUseCase(latLng.latitude, latLng.longitude).onSuccess { footprintSave ->
                _footprintSave.value = footprintSave
            }.onFailure {
            }
        }
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
