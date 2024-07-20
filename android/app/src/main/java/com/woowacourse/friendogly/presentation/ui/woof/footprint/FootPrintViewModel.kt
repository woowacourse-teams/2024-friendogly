package com.woowacourse.friendogly.presentation.ui.woof.footprint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.domain.mapper.toPresentation
import com.woowacourse.friendogly.domain.repository.FootPrintRepository
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.BaseViewModelFactory
import com.woowacourse.friendogly.presentation.model.FootPrintInfoUiModel
import kotlinx.coroutines.launch

class FootPrintViewModel(
    private val memberId: Long,
    private val footPrintRepository: FootPrintRepository,
) : BaseViewModel() {
    private val _uiState: MutableLiveData<FootPrintUiState> =
        MutableLiveData(FootPrintUiState.Loading)
    val uiState: LiveData<FootPrintUiState> get() = _uiState

    private val _footPrintInfo: MutableLiveData<FootPrintInfoUiModel> = MutableLiveData()
    val footPrintInfo: LiveData<FootPrintInfoUiModel> get() = _footPrintInfo

    init {
        viewModelScope.launch {
            loadFootPrintInfo()
        }
    }

    private suspend fun loadFootPrintInfo() {
        footPrintRepository.getFootPrintInfo(memberId).onSuccess { footPrintInfo ->
            _footPrintInfo.value = footPrintInfo.toPresentation()
            _uiState.value = FootPrintUiState.Success(footPrintInfo.toPresentation())
        }.onFailure {
            _uiState.value = FootPrintUiState.Error(it)
        }
    }

    companion object {
        fun factory(
            memberId: Long,
            footPrintRepository: FootPrintRepository,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                FootPrintViewModel(memberId = memberId, footPrintRepository = footPrintRepository)
            }
        }
    }
}
