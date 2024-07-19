package com.woowacourse.friendogly.presentation.ui.woof.footprint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.data.mapper.toUiModel
import com.woowacourse.friendogly.domain.repository.FootPrintRepository
import kotlinx.coroutines.launch

class FootPrintViewModel(
    private val memberId: Long,
    private val footPrintRepository: FootPrintRepository,
) : ViewModel() {
    private val _uiState: MutableLiveData<FootPrintUiState> =
        MutableLiveData(FootPrintUiState.Loading)
    val uiState: LiveData<FootPrintUiState> get() = _uiState

    private val _footPrint: MutableLiveData<FootPrintUiModel> = MutableLiveData()
    val footPrint: LiveData<FootPrintUiModel> get() = _footPrint

    init {
        viewModelScope.launch {
            loadFootPrint()
        }
    }

    private suspend fun loadFootPrint() {
        footPrintRepository.getFootPrint(memberId).onSuccess { footPrint ->
            _footPrint.value = footPrint.toUiModel()
        }.onFailure {
        }
    }
}
