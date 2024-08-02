package com.happy.friendogly.presentation.ui.mylocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.happy.friendogly.domain.model.Address
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

    private val _address: MutableLiveData<Address> = MutableLiveData()
    val address: LiveData<Address> get() = _address

    fun updateAddress(
        adminArea: String,
        subLocality: String,
        thoroughfare: String,
    ) {
        _address.value = Address(
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
        val address = address.value ?: return submitInValidLocation()
        saveLocation(address)
    }

    private fun saveLocation(address: Address) = viewModelScope.launch {
        saveAddressUseCase.invoke(address = address)
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
