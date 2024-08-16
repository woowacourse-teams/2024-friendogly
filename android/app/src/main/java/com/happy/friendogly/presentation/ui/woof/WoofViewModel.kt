package com.happy.friendogly.presentation.ui.woof

import android.location.Address
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.analytics.AnalyticsHelper
import com.happy.friendogly.domain.usecase.GetFootprintInfoUseCase
import com.happy.friendogly.domain.usecase.GetFootprintMarkBtnInfoUseCase
import com.happy.friendogly.domain.usecase.GetNearFootprintsUseCase
import com.happy.friendogly.domain.usecase.PatchWalkStatusUseCase
import com.happy.friendogly.domain.usecase.PostFootprintUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import com.happy.friendogly.presentation.ui.woof.mapper.toPresentation
import com.happy.friendogly.presentation.ui.woof.model.Footprint
import com.happy.friendogly.presentation.ui.woof.model.WalkStatus
import com.happy.friendogly.presentation.ui.woof.uimodel.FootprintInfoUiModel
import com.happy.friendogly.presentation.utils.logFootprintMarkBtnInfo
import com.happy.friendogly.presentation.utils.logNearFootprintSize
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.launch

class WoofViewModel(
    private val analyticsHelper: AnalyticsHelper,
    private val postFootprintUseCase: PostFootprintUseCase,
    private val patchWalkStatusUseCase: PatchWalkStatusUseCase,
    private val getNearFootprintsUseCase: GetNearFootprintsUseCase,
    private val getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
    private val getFootprintInfoUseCase: GetFootprintInfoUseCase,
) : BaseViewModel() {
    private val _nearFootprints: MutableLiveData<List<Footprint>> = MutableLiveData()
    val nearFootprints: LiveData<List<Footprint>> get() = _nearFootprints

    private val _myFootprint: MutableLiveData<Footprint?> = MutableLiveData()
    val myFootprint: LiveData<Footprint?> get() = _myFootprint

    private val _footprintInfo: MutableLiveData<FootprintInfoUiModel> = MutableLiveData()
    val footprintInfo: LiveData<FootprintInfoUiModel> get() = _footprintInfo

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
                val otherFootprints = nearFootprints.filter { footprint -> !footprint.isMine }
                analyticsHelper.logNearFootprintSize(otherFootprints.size)
                _nearFootprints.value = otherFootprints

                val myFootprint = nearFootprints.firstOrNull { footprint -> footprint.isMine }
                _myFootprint.value = myFootprint
            }.onFailure {
            }
        }
    }

    fun loadFootprintMarkBtnInfo() {
        viewModelScope.launch {
            getFootprintMarkBtnInfoUseCase().onSuccess { footPrintMarkBtnInfo ->
                analyticsHelper.logFootprintMarkBtnInfo(
                    footPrintMarkBtnInfo.hasPet,
                    footPrintMarkBtnInfo.remainingTime(),
                )
                if (!footPrintMarkBtnInfo.hasPet) {
                    _snackbarActions.emit(WoofSnackbarActions.ShowHasNotPet)
                } else if (!footPrintMarkBtnInfo.isMarkBtnClickable()) {
                    _snackbarActions.emit(
                        WoofSnackbarActions.ShowCantClickMarkBtn(
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
            postFootprintUseCase(latLng.latitude, latLng.longitude).onSuccess { myFootprint ->
                _myFootprint.value = Footprint(
                    footprintId = myFootprint.footprintId,
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    walkStatus = WalkStatus.BEFORE,
                    createdAt = myFootprint.createdAt,
                    isMine = true,
                )
                _mapActions.value = Event(WoofMapActions.RemoveNearFootprints)
                _mapActions.value = Event(WoofMapActions.HideRegisterMarkerLayout)
                _snackbarActions.emit(WoofSnackbarActions.ShowMarkerRegistered)
                loadNearFootprints(latLng)
            }.onFailure {
            }
        }
    }

    fun updateWalkStatus(latLng: LatLng) {
        viewModelScope.launch {
            patchWalkStatusUseCase(latLng.latitude, latLng.longitude).onSuccess { walkStatus ->
                when (walkStatus) {
                    WalkStatus.BEFORE -> return@onSuccess

                    WalkStatus.ONGOING -> {
                        if (myFootprint.value?.walkStatus == WalkStatus.BEFORE) {
                            _myFootprint.value =
                                myFootprint.value?.copy(walkStatus = WalkStatus.ONGOING)
                        }
                    }

                    WalkStatus.AFTER ->
                        if (myFootprint.value?.walkStatus == WalkStatus.ONGOING) {
                            _myFootprint.value =
                                myFootprint.value?.copy(walkStatus = WalkStatus.AFTER)
                        }
                }
            }.onFailure {
            }
        }
    }

    fun loadFootprintInfo(footprintId: Long) {
        viewModelScope.launch {
            getFootprintInfoUseCase(footprintId).onSuccess { footprintInfo ->
                _footprintInfo.value = footprintInfo.toPresentation()
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
        val addressLine = address.getAddressLine(0).replace("대한민국", "").trimStart()
        _registerAddress.value = addressLine
    }

    // API 나오면 수정
    fun endWalk() {
        _myFootprint.value = myFootprint.value?.copy(walkStatus = WalkStatus.AFTER)
        _snackbarActions.value = Event(WoofSnackbarActions.ShowEndWalk)
    }

    companion object {
        fun factory(
            analyticsHelper: AnalyticsHelper,
            postFootprintUseCase: PostFootprintUseCase,
            patchWalkStatusUseCase: PatchWalkStatusUseCase,
            getNearFootprintsUseCase: GetNearFootprintsUseCase,
            getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
            getFootprintInfoUseCase: GetFootprintInfoUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                WoofViewModel(
                    analyticsHelper = analyticsHelper,
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
