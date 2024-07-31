package com.happy.friendogly.presentation.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.usecase.DeleteLocalDataUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import kotlinx.coroutines.launch

class SettingViewModel(
    private val deleteLocalDataUseCase: DeleteLocalDataUseCase,
) : BaseViewModel() {
    private val _alarmPushPermitted: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
    val alarmPushPermitted: LiveData<Boolean> get() = _alarmPushPermitted

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
            deleteLocalDataUseCase().onSuccess {
                _navigateAction.emit(SettingNavigationAction.NavigateToRegister)
            }.onFailure {
                // TODO 예외처리
            }
        }
    }

    fun navigateToUnsubscribe() {
        viewModelScope.launch {
            deleteLocalDataUseCase().onSuccess {
                _navigateAction.emit(SettingNavigationAction.NavigateToRegister)
            }.onFailure {
                // TODO 예외처리
            }
        }
    }

    companion object {
        fun factory(deleteLocalDataUseCase: DeleteLocalDataUseCase): ViewModelProvider.Factory {
            return BaseViewModelFactory { _ ->
                SettingViewModel(
                    deleteLocalDataUseCase = deleteLocalDataUseCase,
                )
            }
        }
    }
}
