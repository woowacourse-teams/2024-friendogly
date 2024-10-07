package com.happy.friendogly.presentation.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.fold
import com.happy.friendogly.domain.usecase.DeleteMemberUseCase
import com.happy.friendogly.domain.usecase.DeleteTokenUseCase
import com.happy.friendogly.domain.usecase.GetChatAlarmUseCase
import com.happy.friendogly.domain.usecase.GetWoofAlarmUseCase
import com.happy.friendogly.domain.usecase.PostLogoutUseCase
import com.happy.friendogly.domain.usecase.SaveChatAlarmUseCase
import com.happy.friendogly.domain.usecase.SaveWoofAlarmUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
    @Inject
    constructor(
        private val deleteTokenUseCase: DeleteTokenUseCase,
        private val getChatAlarmUseCase: GetChatAlarmUseCase,
        private val getWoofAlarmUseCase: GetWoofAlarmUseCase,
        private val saveChatAlarmUseCase: SaveChatAlarmUseCase,
        private val saveWoofAlarmUseCase: SaveWoofAlarmUseCase,
        private val deleteMemberUseCase: DeleteMemberUseCase,
        private val postLogoutUseCase: PostLogoutUseCase,
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

        init {
            setAlarmSetting()
        }

        private fun setAlarmSetting() {
            launch {
                getChatAlarmUseCase().onSuccess {
                    _uiState.value =
                        _uiState.value?.copy(
                            chattingAlarmPushPermitted = it,
                        )
                }.onFailure {
                    // TODO 에러핸들링
                }
            }

            launch {
                getWoofAlarmUseCase().onSuccess {
                    _uiState.value =
                        _uiState.value?.copy(
                            woofAlarmPushPermitted = it,
                        )
                }.onFailure {
                    // TODO 에러핸들링
                }
            }
        }

        fun saveChattingAlarmSetting(checked: Boolean) {
            launch {
                saveChatAlarmUseCase(checked)
            }
        }

        fun saveWoofAlarmSetting(checked: Boolean) {
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
                logout()
                _loading.emit(false)
            }
        }

        private suspend fun logout() {
            postLogoutUseCase().fold(
                onSuccess = {
                    deleteToken()
                },
                onError = { error ->
                    when (error) {
                        DataError.Network.NO_INTERNET -> _message.emit(SettingMessage.NoInternetMessage)
                        else -> _message.emit(SettingMessage.ServerErrorMessage)
                    }
                },
            )
        }

        fun navigateToUnsubscribe() {
            launch {
                _loading.emit(true)
                unsubscribe()
                _loading.emit(false)
            }
        }

        private suspend fun unsubscribe() {
            deleteMemberUseCase().fold(
                onSuccess = {
                    deleteToken()
                },
                onError = { error ->
                    when (error) {
                        DataError.Network.NO_INTERNET -> _message.emit(SettingMessage.NoInternetMessage)
                        else -> _message.emit(SettingMessage.ServerErrorMessage)
                    }
                },
            )
        }

        private suspend fun deleteToken() {
            deleteTokenUseCase().fold(
                onSuccess = {
                    _navigateAction.emit(SettingNavigationAction.NavigateToRegister)
                },
                onError = { error ->
                    when (error) {
                        DataError.Local.TOKEN_NOT_STORED -> _message.emit(SettingMessage.TokenNotStoredErrorMessage)
                        else -> _message.emit(SettingMessage.DefaultErrorMessage)
                    }
                },
            )
        }
    }
