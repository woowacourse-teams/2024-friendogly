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
import com.woowacourse.friendogly.presentation.model.FootprintMarkBtnInfoUiModel
import com.woowacourse.friendogly.presentation.model.FootprintUiModel
import kotlinx.coroutines.launch

class WoofViewModel(
    private val postFootprintUseCase: PostFootprintUseCase,
    private val getNearFootprintsUseCase: GetNearFootprintsUseCase,
    private val getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
    private val getLandMarksUseCase: GetLandMarksUseCase,
) :
    BaseViewModel() {
    private val _uiState: MutableLiveData<WoofUiState> = MutableLiveData()
    val uiState: LiveData<WoofUiState> get() = _uiState

    fun markFootprint(latLng: LatLng) {
        viewModelScope.launch {
            getFootprintMarkBtnInfoUseCase().onSuccess { footPrintMarkBtnInfo ->
                if (footPrintMarkBtnInfo.isMarkBtnClickable()) {
                    loadNearFootprints(latLng, footPrintMarkBtnInfo.toPresentation())
                }
            }.onFailure {
            }
        }
    }

    private fun loadNearFootprints(
        latLng: LatLng,
        footprintMarkBtnInfo: FootprintMarkBtnInfoUiModel,
    ) {
        viewModelScope.launch {
            getNearFootprintsUseCase(latLng.latitude, latLng.longitude)
                .onSuccess { nearFootPrints ->
                    markMyFootprint(latLng, footprintMarkBtnInfo, nearFootPrints.toPresentation())
                }.onFailure {
                }
        }
    }

    private fun markMyFootprint(
        latLng: LatLng,
        footprintMarkBtnInfo: FootprintMarkBtnInfoUiModel,
        nearFootprints: List<FootprintUiModel>,
    ) {
        viewModelScope.launch {
            postFootprintUseCase(latLng.latitude, latLng.longitude).onSuccess {
                val state = uiState.value ?: WoofUiState()
                _uiState.value =
                    state.copy(
                        footprintMarkBtnInfo = footprintMarkBtnInfo,
                        nearFootprints = nearFootprints,
                    )
            }.onFailure {
            }
        }
    }

    fun loadLandMarks() {
        viewModelScope.launch {
            getLandMarksUseCase().onSuccess { landMarks ->
                val state = uiState.value ?: return@onSuccess
                _uiState.value = state.copy(landMarks = landMarks.toPresentation())
            }.onFailure {
            }
        }
    }

    companion object {
        fun factory(
            postFootprintUseCase: PostFootprintUseCase,
            getNearFootprintsUseCase: GetNearFootprintsUseCase,
            getFootprintMarkBtnInfoUseCase: GetFootprintMarkBtnInfoUseCase,
            getLandMarksUseCase: GetLandMarksUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                WoofViewModel(
                    postFootprintUseCase = postFootprintUseCase,
                    getNearFootprintsUseCase = getNearFootprintsUseCase,
                    getFootprintMarkBtnInfoUseCase = getFootprintMarkBtnInfoUseCase,
                    getLandMarksUseCase = getLandMarksUseCase,
                )
            }
        }
    }
}
