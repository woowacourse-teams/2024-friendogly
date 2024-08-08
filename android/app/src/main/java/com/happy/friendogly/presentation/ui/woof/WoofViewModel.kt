package com.happy.friendogly.presentation.ui.woof

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.GetFootprintInfoUseCase
import com.happy.friendogly.domain.usecase.GetFootprintMarkBtnInfoUseCase
import com.happy.friendogly.domain.usecase.GetNearFootprintsUseCase
import com.happy.friendogly.domain.usecase.PatchWalkStatusUseCase
import com.happy.friendogly.domain.usecase.PostFootprintUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.woof.mapper.toPetDetailsPresentation
import com.happy.friendogly.presentation.ui.woof.mapper.toWalkStatusPresentation
import com.happy.friendogly.presentation.ui.woof.model.Footprint
import com.happy.friendogly.presentation.ui.woof.model.MyFootprint
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoPetDetailUiModel
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoWalkStatusUiModel
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.launch

class WoofViewModel(
    private val postFootprintUseCase: PostFootprintUseCase,
    private val patchWalkStatusUseCase: PatchWalkStatusUseCase,
    private val getNearFootprintsUseCase: GetNearFootprintsUseCase,
    private val getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
    private val getFootprintInfoUseCase: GetFootprintInfoUseCase,
) : BaseViewModel() {
    private val _nearFootprints: MutableLiveData<List<Footprint>> = MutableLiveData()
    val nearFootprints: LiveData<List<Footprint>> get() = _nearFootprints

    private val _myFootprint: MutableLiveData<MyFootprint?> = MutableLiveData()
    val myFootprint: LiveData<MyFootprint?> get() = _myFootprint

    private val _myWalkStatus: MutableLiveData<WalkStatus?> = MutableLiveData()
    val myWalkStatus: LiveData<WalkStatus?> get() = _myWalkStatus

    private val _footprintInfoWalkStatus: MutableLiveData<FootprintInfoWalkStatusUiModel> =
        MutableLiveData()
    val footprintInfoWalkStatus: LiveData<FootprintInfoWalkStatusUiModel> get() = _footprintInfoWalkStatus

    private val _footprintInfoPetDetails: MutableLiveData<List<FootprintInfoPetDetailUiModel>> =
        MutableLiveData()
    val footprintInfoPetDetails: LiveData<List<FootprintInfoPetDetailUiModel>> get() = _footprintInfoPetDetails

    private val _registerAddress: MutableLiveData<String> = MutableLiveData()
    val registerAddress: LiveData<String> get() = _registerAddress

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
                val myFootprint = nearFootprints.find { footprint -> footprint.isMine }
                if (myFootprint != null) {
                    _myFootprint.value =
                        MyFootprint(
                            footprintId = myFootprint.footprintId,
                            latitude = myFootprint.latitude,
                            longitude = myFootprint.longitude,
                            createdAt = myFootprint.createdAt,
                        )
                } else {
                    _myFootprint.value = null
                    _myWalkStatus.value = null
                }
                _nearFootprints.value = nearFootprints.filter { footprint -> !footprint.isMine }
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

    fun markFootprint(latLng: LatLng) {
        viewModelScope.launch {
            postFootprintUseCase(latLng.latitude, latLng.longitude).onSuccess { footprintSave ->
                _myFootprint.value = footprintSave
                _mapActions.value = Event(WoofMapActions.RemoveNearFootprints)
                _mapActions.value = Event(WoofMapActions.HideRegisterMarkerLayout)
                _snackbarActions.emit(WoofSnackbarActions.ShowMarkerRegistered)
                _myWalkStatus.value = WalkStatus.BEFORE
                loadNearFootprints(latLng)
            }.onFailure {
            }
        }
    }

    fun updateWalkStatus(latLng: LatLng) {
        viewModelScope.launch {
            patchWalkStatusUseCase(latLng.latitude, latLng.longitude).onSuccess { walkStatus ->
                when (walkStatus) {
                    WalkStatus.BEFORE -> {
                        if (myWalkStatus.value == null) {
                            _myWalkStatus.value = WalkStatus.BEFORE
                        }
                    }

                    WalkStatus.ONGOING -> {
                        if (myWalkStatus.value == null || myWalkStatus.value == WalkStatus.BEFORE) {
                            _myWalkStatus.value = WalkStatus.ONGOING
                            _snackbarActions.emit(
                                WoofSnackbarActions.ShowOnGoingWalkStatusSnackbar,
                            )
                        }
                    }

                    WalkStatus.AFTER ->
                        if (myWalkStatus.value == WalkStatus.ONGOING) {
                            _myWalkStatus.value = WalkStatus.AFTER
                            _snackbarActions.emit(
                                WoofSnackbarActions.ShowAfterWalkStatusSnackbar,
                            )
                        }
                }
            }.onFailure {
            }
        }
    }

    fun loadFootprintInfo(footprintId: Long) {
        viewModelScope.launch {
            getFootprintInfoUseCase(footprintId).onSuccess { footPrintInfo ->
                _footprintInfoWalkStatus.value = footPrintInfo.toWalkStatusPresentation()
                _footprintInfoPetDetails.value = footPrintInfo.toPetDetailsPresentation()
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

    fun loadAddress(address: Address) {
        _registerAddress.value = address.getAddressLine(0).substring(5)
    }

    fun saveLowLevelSdkAddress(
        geocoder: Geocoder,
        latitude: Double,
        longitude: Double,
    ) = viewModelScope.launch {
        runCatching {
            geocoder.getFromLocation(latitude, longitude, 1)
                ?: return@launch submitInValidLocation()
        }
            .onSuccess { address ->
                loadAddress(address[0])
            }
            .onFailure {
                submitInValidLocation()
            }
    }

    private fun submitInValidLocation() {
        _snackbarActions.emit(WoofSnackbarActions.ShowInvalidLocationSnackbar)
    }

    companion object {
        fun factory(
            postFootprintUseCase: PostFootprintUseCase,
            patchWalkStatusUseCase: PatchWalkStatusUseCase,
            getNearFootprintsUseCase: GetNearFootprintsUseCase,
            getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
            getFootprintInfoUseCase: GetFootprintInfoUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                WoofViewModel(
                    postFootprintUseCase = postFootprintUseCase,
                    patchWalkStatusUseCase = patchWalkStatusUseCase,
                    getNearFootprintsUseCase = getNearFootprintsUseCase,
                    getFootprintMarkBtnInfoUseCase = getFootprintMarkBtnInfoUseCase,
                    getFootprintInfoUseCase = getFootprintInfoUseCase,
                )
            }
        }
    }
}
