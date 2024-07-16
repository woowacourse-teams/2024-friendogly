package com.woowacourse.friendogly.presentation.ui.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.vectormap.LatLng
import com.woowacourse.friendogly.data.repository.HackathonRepository
import kotlinx.coroutines.launch

class WoofViewModel : ViewModel() {
    private val _dogInfo: MutableLiveData<WoofDogUiModel> = MutableLiveData()
    val dogInfo: LiveData<WoofDogUiModel> get() = _dogInfo

    private val _dogs: MutableLiveData<List<DogFootUiModel>> = MutableLiveData()
    val dogs: LiveData<List<DogFootUiModel>> get() = _dogs

    fun getDogInfo(memberId: Long) {
        viewModelScope.launch {
            _dogInfo.value = HackathonRepository.getPet(memberId).getOrThrow().first().toUiModel()
        }
    }

    fun getFoots(latLng: LatLng) {
        viewModelScope.launch {
            _dogs.value = HackathonRepository.getFoots(latLng).map { it.toUiModel() }
        }
    }

    fun findFoot(latLng: LatLng): Long {
        return dogs.value?.find {
            it.latitude == latLng.latitude && it.longitude == latLng.longitude
        }?.memberId ?: 1L
    }


}
