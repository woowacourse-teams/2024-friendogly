package com.happy.friendogly.presentation.ui.mylocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.model.UserAddress
import com.happy.friendogly.domain.usecase.SaveAddressUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory
import com.happy.friendogly.presentation.base.Event
import com.happy.friendogly.presentation.base.emit
import kotlinx.coroutines.launch

class SettingMyLocationViewModel(
    private val saveAddressUseCase: SaveAddressUseCase,
) : BaseViewModel(), SettingMyLocationActionHandler {
    private val _event: MutableLiveData<Event<SettingMyLocationEvent>> = MutableLiveData()
    val event: LiveData<Event<SettingMyLocationEvent>> get() = _event

    private val _User_address: MutableLiveData<UserAddress> = MutableLiveData()
    val userAddress: LiveData<UserAddress> get() = _User_address

    fun updateAddress(
        adminArea: String,
        subLocality: String,
        thoroughfare: String,
    ) {
        _User_address.value = UserAddress(
            adminArea = adminArea,
            subLocality = subLocality,
            thoroughfare = thoroughfare,
        )
    }

    companion object {
        fun factory(
            saveAddressUseCase: SaveAddressUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                SettingMyLocationViewModel(
                    saveAddressUseCase = saveAddressUseCase
                )
            }
        }
    }

    override fun closeSelect() {
        _event.emit(SettingMyLocationEvent.Navigation.NavigateToPrev)
    }

    override fun submitSelect() {
        val address = userAddress.value ?: return submitInValidLocation()
        saveLocation(address)
    }

    private fun saveLocation(userAddress: UserAddress) = viewModelScope.launch {
        saveAddressUseCase.invoke(userAddress = userAddress)
            .onSuccess {
                _event.emit(SettingMyLocationEvent.Navigation.NavigateToPrev)
            }
            .onFailure {
                submitInValidLocation()
            }
    }

    private fun submitInValidLocation() {
        _event.emit(SettingMyLocationEvent.InvalidLocation)
    }

}
