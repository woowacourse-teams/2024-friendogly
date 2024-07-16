package com.woowacourse.friendogly.presentation.ui.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.data.repository.HackathonRepository
import kotlinx.coroutines.launch

class WoofViewModel : ViewModel() {

    private val _dogInfo: MutableLiveData<WoofDogUiModel> = MutableLiveData()
    val dogInfo: LiveData<WoofDogUiModel> get() = _dogInfo

    fun getDogInfo(memberId: Long) {
        viewModelScope.launch {
            _dogInfo.value = HackathonRepository.getPet(memberId).first().toUiModel()
        }
    }
}
