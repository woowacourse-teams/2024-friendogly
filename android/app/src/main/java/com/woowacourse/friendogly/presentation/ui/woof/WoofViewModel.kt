package com.woowacourse.friendogly.presentation.ui.woof

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.woowacourse.friendogly.domain.mapper.toUiModel
import com.woowacourse.friendogly.domain.repository.WoofRepository
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.BaseViewModelFactory
import kotlinx.coroutines.launch

class WoofViewModel(private val woofRepository: WoofRepository) :
    BaseViewModel() {
    private val _uiState: MutableLiveData<WoofUiState> = MutableLiveData(WoofUiState())
    val uiState: LiveData<WoofUiState> get() = _uiState

    fun loadNearFootPrints(latLng: LatLng) {
        viewModelScope.launch {
            woofRepository.getNearFootPrints(latLng).onSuccess { nearFootPrints ->
                val state = uiState.value ?: return@onSuccess
                _uiState.postValue(state.copy(nearFootPrints = nearFootPrints.toUiModel()))
            }.onFailure {
            }
        }
    }

    fun loadLandMarks() {
        viewModelScope.launch {
            woofRepository.getLandMarks().onSuccess { landMarks ->
                val state = uiState.value ?: return@onSuccess
                _uiState.postValue(state.copy(landMarks = landMarks.toUiModel()))
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
