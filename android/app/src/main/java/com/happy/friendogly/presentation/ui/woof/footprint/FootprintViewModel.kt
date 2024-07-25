package com.happy.friendogly.presentation.ui.woof.footprint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.mapper.toPresentation
import com.happy.friendogly.domain.usecase.GetFootprintInfoUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import kotlinx.coroutines.launch

class FootprintViewModel(
    private val footprintId: Long,
    private val getFootprintInfoUseCase: GetFootprintInfoUseCase,
) : BaseViewModel() {
    private val _uiState: MutableLiveData<FootprintUiState> = MutableLiveData()
    val uiState: LiveData<FootprintUiState> get() = _uiState

    init {
        viewModelScope.launch {
            loadFootPrintInfo()
        }
    }

    private suspend fun loadFootPrintInfo() {
        getFootprintInfoUseCase(footprintId).onSuccess { footPrintInfo ->
            val state = uiState.value ?: FootprintUiState()
            _uiState.value = state.copy(footPrintInfo = footPrintInfo.toPresentation())
        }.onFailure {
        }
    }

    companion object {
        fun factory(
            footPrintId: Long,
            getFootprintInfoUseCase: GetFootprintInfoUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                FootprintViewModel(
                    footprintId = footPrintId,
                    getFootprintInfoUseCase = getFootprintInfoUseCase,
                )
            }
        }
    }
}
