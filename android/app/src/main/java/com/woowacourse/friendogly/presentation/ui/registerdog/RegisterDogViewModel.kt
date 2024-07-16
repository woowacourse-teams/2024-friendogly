package com.woowacourse.friendogly.presentation.ui.registerdog

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.woowacourse.friendogly.presentation.base.BaseViewModel
import com.woowacourse.friendogly.presentation.base.Event
import com.woowacourse.friendogly.presentation.base.emit
import okhttp3.MultipartBody

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
        _navigateAction.emit(RegisterDogNavigationAction.NavigateToMyPage)
    }
}
