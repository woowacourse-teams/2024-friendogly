package com.happy.friendogly.presentation.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.usecase.DeleteTokenUseCase
import com.happy.friendogly.domain.usecase.SaveChatAlarmUseCase
import com.happy.friendogly.domain.usecase.SaveWoofAlarmUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit

class SettingViewModel(
    private val deleteTokenUseCase: DeleteTokenUseCase,
    private val saveChatAlarmUseCase: SaveChatAlarmUseCase,
    private val saveWoofAlarmUseCase: SaveWoofAlarmUseCase,
) : BaseViewModel() {
    private val _uiState: MutableLiveData<SettingUiState> =
        MutableLiveData<SettingUiState>(SettingUiState())
    val uiState: LiveData<SettingUiState> get() = _uiState

    private val _navigateAction: MutableLiveData<Event<SettingNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<SettingNavigationAction>> get() = _navigateAction

    private val _message: MutableLiveData<Event<SettingMessage>> = MutableLiveData(null)
    val message: LiveData<Event<SettingMessage>> get() = _message

    private val _loading: MutableLiveData<Event<Boolean>> = MutableLiveData(null)
    val loading: LiveData<Event<Boolean>> get() = _loading

    fun onTotalAlarmToggled(checked: Boolean) {}

    fun onChattingAlarmToggled(checked: Boolean) {
        launch {
            saveChatAlarmUseCase(checked)
        }
    }

    fun onClubAlarmToggled(checked: Boolean) {}

    fun onWoofAlarmToggled(checked: Boolean) {
        launch {
            saveWoofAlarmUseCase(checked)
        }
    }

    fun navigateToBack() {
        _navigateAction.emit(SettingNavigationAction.NavigateToBack)
    }

    fun navigateToAppInfo() {
        _navigateAction.emit(SettingNavigationAction.NavigateToAppInfo)
    }

    fun navigateToPrivacyPolicy() {
        _navigateAction.emit(SettingNavigationAction.NavigateToPrivacyPolicy)
    }

    fun navigateToLogoutDialog() {
        _navigateAction.emit(SettingNavigationAction.NavigateToLogout)
    }

    fun navigateToUnsubscribeDialog() {
        _navigateAction.emit(SettingNavigationAction.NavigateToUnsubscribe)
    }

    fun navigateToLogout() {
        launch {
            _loading.emit(true)
            deleteTokenUseCase().fold(
                onSuccess = {
                    _loading.emit(false)
                    _navigateAction.emit(SettingNavigationAction.NavigateToRegister)
                },
                onError = { error ->
                    _loading.emit(false)
                    when (error) {
                        DataError.Local.TOKEN_NOT_STORED -> _message.emit(SettingMessage.TokenNotStoredErrorMessage)
                        else -> _message.emit(SettingMessage.DefaultErrorMessage)
                    }
                },
            )
        }
    }

    fun navigateToUnsubscribe() {
        launch {
            _loading.emit(true)
            deleteTokenUseCase().fold(
                onSuccess = {
                    _loading.emit(false)
                    _navigateAction.emit(SettingNavigationAction.NavigateToRegister)
                },
                onError = { error ->
                    _loading.emit(false)
                    when (error) {
                        DataError.Local.TOKEN_NOT_STORED -> _message.emit(SettingMessage.TokenNotStoredErrorMessage)
                        else -> _message.emit(SettingMessage.DefaultErrorMessage)
                    }
                },
            )
        }
    }

    companion object {
        fun factory(
            saveChatAlarmUseCase: SaveChatAlarmUseCase,
            saveWoofAlarmUseCase: SaveWoofAlarmUseCase,
            deleteTokenUseCase: DeleteTokenUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                SettingViewModel(
                    saveChatAlarmUseCase = saveChatAlarmUseCase,
                    saveWoofAlarmUseCase = saveWoofAlarmUseCase,
                    deleteTokenUseCase = deleteTokenUseCase,
                )
            }
        }
    }
}
