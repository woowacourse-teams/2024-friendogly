package com.happy.friendogly.presentation.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.DeleteTokenUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import kotlinx.coroutines.launch

class SettingViewModel(
    private val deleteTokenUseCase: DeleteTokenUseCase,
) : BaseViewModel() {
    private val _uiState: MutableLiveData<SettingUiState> = MutableLiveData<SettingUiState>(SettingUiState())
    val uiState: LiveData<SettingUiState> get() = _uiState

    private val _navigateAction: MutableLiveData<Event<SettingNavigationAction>> =
        MutableLiveData(null)
    val navigateAction: LiveData<Event<SettingNavigationAction>> get() = _navigateAction

    fun onTotalAlarmToggled(checked: Boolean) {}

    fun onChattingAlarmToggled(checked: Boolean) {}

    fun onClubAlarmToggled(checked: Boolean) {}

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
        viewModelScope.launch {
            deleteTokenUseCase().onSuccess {
                _navigateAction.emit(SettingNavigationAction.NavigateToRegister)
            }.onFailure {
                // TODO 예외처리
            }
        }
    }

    fun navigateToUnsubscribe() {
        viewModelScope.launch {
            deleteTokenUseCase().onSuccess {
                _navigateAction.emit(SettingNavigationAction.NavigateToRegister)
            }.onFailure {
                // TODO 예외처리
            }
        }
    }

    companion object {
        fun factory(deleteTokenUseCase: DeleteTokenUseCase): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                SettingViewModel(
                    deleteTokenUseCase = deleteTokenUseCase,
                )
            }
        }
    }
}
