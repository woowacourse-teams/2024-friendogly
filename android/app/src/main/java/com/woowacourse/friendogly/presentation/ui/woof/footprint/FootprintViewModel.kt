package com.woowacourse.friendogly.presentation.ui.woof.footprint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.domain.mapper.toPresentation
import com.woowacourse.friendogly.domain.repository.FootprintRepository
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.BaseViewModelFactory
import kotlinx.coroutines.launch

class FootprintViewModel(
    private val footprintId: Long,
    private val footPrintRepository: FootprintRepository,
) : BaseViewModel() {
    private val _uiState: MutableLiveData<FootprintUiState> = MutableLiveData()
    val uiState: LiveData<FootprintUiState> get() = _uiState

    init {
        viewModelScope.launch {
            loadFootPrintInfo()
        }
    }

    private suspend fun loadFootPrintInfo() {
        footPrintRepository.getFootPrintInfo(footprintId).onSuccess { footPrintInfo ->
            val state = uiState.value ?: FootprintUiState()
            _uiState.postValue(state.copy(footPrintInfo = footPrintInfo.toPresentation()))
        }.onFailure {
        }
    }

    companion object {
        fun factory(
            footPrintId: Long,
            footPrintRepository: FootprintRepository,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                FootprintViewModel(
                    footprintId = footPrintId,
                    footPrintRepository = footPrintRepository,
                )
            }
        }
    }
}
