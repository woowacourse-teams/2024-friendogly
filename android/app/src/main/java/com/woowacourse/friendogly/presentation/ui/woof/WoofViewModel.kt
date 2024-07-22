package com.woowacourse.friendogly.presentation.ui.woof

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.woowacourse.friendogly.domain.mapper.toPresentation
import com.woowacourse.friendogly.domain.repository.WoofRepository
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.BaseViewModelFactory
import kotlinx.coroutines.launch

class WoofViewModel(private val woofRepository: WoofRepository) :
    BaseViewModel() {
    private val _uiState: MutableLiveData<WoofUiState> = MutableLiveData()
    val uiState: LiveData<WoofUiState> get() = _uiState

    private val _isFootPrintMarkBtnInfoLoaded: MutableLiveData<Boolean> = MutableLiveData(false)
    val isFootPrintMarkBtnInfoLoaded: LiveData<Boolean> get() = _isFootPrintMarkBtnInfoLoaded

    fun loadMarkFootPrintBtnInfo() {
        viewModelScope.launch {
            woofRepository.getFootPrintMarkBtnInfo().onSuccess { footPrintMarkBtnInfo ->
                val state = uiState.value ?: WoofUiState()
                _uiState.postValue(state.copy(footPrintMarkBtnInfo = footPrintMarkBtnInfo.toPresentation()))
                _isFootPrintMarkBtnInfoLoaded.postValue(true)
            }.onFailure {
            }
        }
    }

    fun markFootPrint(latLng: LatLng) {
        viewModelScope.launch {
            woofRepository.postFootPrint(latLng.latitude, latLng.longitude).onSuccess {
                loadNearFootPrints(latLng)
            }.onFailure {
            }
        }
    }

    fun loadNearFootPrints(latLng: LatLng) {
        viewModelScope.launch {
            woofRepository.getNearFootPrints(latLng.latitude, latLng.longitude)
                .onSuccess { nearFootPrints ->
                    val state = uiState.value ?: WoofUiState()
                    _uiState.postValue(state.copy(nearFootPrints = nearFootPrints.toPresentation()))
                }.onFailure {
                }
        }
    }

    fun loadLandMarks() {
        viewModelScope.launch {
            woofRepository.getLandMarks().onSuccess { landMarks ->
                val state = uiState.value ?: return@onSuccess
                _uiState.postValue(state.copy(landMarks = landMarks.toPresentation()))
            }.onFailure {
            }
        }
    }

    companion object {
        fun factory(woofRepository: WoofRepository): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                WoofViewModel(woofRepository)
            }
        }
    }
}
