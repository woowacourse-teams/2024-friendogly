package com.woowacourse.friendogly.presentation.ui.registerdog

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.woowacourse.friendogly.data.repository.HackathonRepository
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit
import com.woowacourse.friendogly.presentation.ui.profilesetting.memberId
import com.woowacourse.friendogly.remote.dto.request.RequestPetPostDto
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.time.LocalDate

class RegisterDogViewModel : BaseViewModel() {
    private val _uiState: MutableLiveData<RegisterDogUiState> =
        MutableLiveData(RegisterDogUiState())
    val uiState: LiveData<RegisterDogUiState> get() = _uiState

    val dogName = MutableLiveData<String>("")
    val oneLiner = MutableLiveData<String>("")

    private val _navigateAction: MutableLiveData<Event<RegisterDogNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<RegisterDogNavigationAction>> get() = _navigateAction

    fun selectDogProfileImage() {
        _navigateAction.emit(RegisterDogNavigationAction.NavigateToSetProfileImage)
    }

    fun updateProfileImage(bitmap: Bitmap) {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(profileImage = bitmap)
    }

    fun resetProfileImage() {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(profileImage = null)
    }

    fun updateProfileFile(file: MultipartBody.Part) {
        val state = _uiState.value ?: return
        _uiState.value = state.copy(profilePath = file)
    }

    fun registerDog() {
        viewModelScope.launch {
            val request =
                RequestPetPostDto(
                    memberId = memberId,
                    name = dogName.value.toString(),
                    description = oneLiner.value.toString(),
                    birthDate = LocalDate.now(),
                    sizeType = "소형견",
                    gender = "수컷",
                    isNeutered = true,
                    image = "https://p16-capcut-sign-va.ibyteimg.com/tos-alisg-v-643f9f/ea8e67b6cfa74848a56381acfe3d00bd~tplv-nhvfeczskr-1:250:0.webp?lk3s=44acef4b&x-expires=1739388125&x-signature=Ut6Jwk42HsiNgt9qGNhS%2B%2BaXZ5w%3D",
                )
            HackathonRepository.postPet(request).onSuccess {
                Log.d("ttt", it.toString())

                _navigateAction.emit(RegisterDogNavigationAction.NavigateToMyPage)
            }.onFailure {
                Log.d("ttt", it.toString())
            }
        }
    }
}
