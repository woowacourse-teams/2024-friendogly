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
import com.happy.friendogly.presentation.ui.woof.mapper.toPetDetailsUiModel
import com.happy.friendogly.presentation.ui.woof.mapper.toWalkStatusUiModel
import com.happy.friendogly.presentation.ui.woof.model.Footprint
import com.happy.friendogly.presentation.ui.woof.model.FootprintSave
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoPetDetailUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoWalkStatusUiModel
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

    private val _footprintWalkStatus: MutableLiveData<FootprintInfoWalkStatusUiModel> =
        MutableLiveData()
    val footprintWalkStatus: LiveData<FootprintInfoWalkStatusUiModel> get() = _footprintWalkStatus

    private val _footprintPetDetails: MutableLiveData<List<FootprintInfoPetDetailUiModel>> =
        MutableLiveData()
    val footprintPetDetails: LiveData<List<FootprintInfoPetDetailUiModel>> get() = _footprintPetDetails

    private val _address: MutableLiveData<String> = MutableLiveData()
    val address: LiveData<String> get() = _address

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
                _nearFootprints.value =
                    nearFootprints.filter { footprint ->
                        !footprint.isMine
                    }
            }.onFailure {
            }
        }
    }

    fun loadFootprintInfo(footprintId: Long) {
        viewModelScope.launch {
            getFootprintInfoUseCase(footprintId).onSuccess { footPrintInfo ->
                _footprintWalkStatus.value = footPrintInfo.toWalkStatusUiModel()
                _footprintPetDetails.value = footPrintInfo.toPetDetailsUiModel()
            }.onFailure {
            }
        }
    }

    fun loadFootprintMarkBtnInfo() {
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
                    _mapActions.emit(WoofMapActions.ShowRegisterMarkerLayout)
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

    fun markFootprint(latLng: LatLng) {
        viewModelScope.launch {
            postFootprintUseCase(latLng.latitude, latLng.longitude).onSuccess { footprintSave ->
                _footprintSave.value = footprintSave
                _mapActions.value = Event(WoofMapActions.RemoveNearFootprints)
                _mapActions.value = Event(WoofMapActions.HideRegisterMarkerLayout)
                _snackbarActions.emit(WoofSnackbarActions.ShowMarkerRegistered)
                loadNearFootprints(latLng)
            }.onFailure {
            }
        }
    }

    fun loadAddress(address: String) {
        _address.value = address
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
