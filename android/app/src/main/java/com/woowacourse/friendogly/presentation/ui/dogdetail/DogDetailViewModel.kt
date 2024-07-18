package com.woowacourse.friendogly.presentation.ui.dogdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.ui.mypage.MyPageViewModel.Companion.dogs

class DogDetailViewModel : BaseViewModel() {
    private val _uiState: MutableLiveData<DogDetailUiState> = MutableLiveData(DogDetailUiState())
    val uiState: LiveData<DogDetailUiState> get() = _uiState

    private val _currentPage: MutableLiveData<Int> = MutableLiveData(Int.MAX_VALUE / 2)
    val currentPage: LiveData<Int> get() = _currentPage

    init {
        fetchDummy()
    }

    private fun fetchDummy() {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(dogs = dogs)
        _currentPage.value = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % dogs.size
    }

    fun updateCurrentPage(page: Int) {
        _currentPage.value = page
    }
}
