package com.woowacourse.friendogly.presentation.ui.woof.footprint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.domain.mapper.toPresentation
import com.woowacourse.friendogly.domain.repository.FootPrintRepository
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.BaseViewModelFactory
import kotlinx.coroutines.launch

class FootPrintViewModel(
    private val footPrintId: Long,
    private val footPrintRepository: FootPrintRepository,
) : BaseViewModel() {
    private val _uiState: MutableLiveData<FootPrintUiState> = MutableLiveData()
    val uiState: LiveData<FootPrintUiState> get() = _uiState

    init {
        viewModelScope.launch {
            loadFootPrintInfo()
        }
    }

    private suspend fun loadFootPrintInfo() {
        footPrintRepository.getFootPrintInfo(footPrintId).onSuccess { footPrintInfo ->
            val state = uiState.value ?: FootPrintUiState()
            _uiState.postValue(state.copy(footPrintInfo = footPrintInfo.toPresentation()))
        }.onFailure {
        }
    }

    companion object {
        fun factory(
            footPrintId: Long,
            footPrintRepository: FootPrintRepository,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                FootPrintViewModel(
                    footPrintId = footPrintId,
                    footPrintRepository = footPrintRepository,
                )
            }
        }
    }
}
