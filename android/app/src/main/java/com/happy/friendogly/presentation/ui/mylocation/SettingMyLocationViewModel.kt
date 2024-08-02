package com.happy.friendogly.presentation.ui.mylocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.happy.friendogly.domain.model.Address
import com.happy.friendogly.domain.usecase.GetAddressUseCase
import com.happy.friendogly.presentation.base.BaseViewModel
import com.happy.friendogly.presentation.base.BaseViewModelFactory

class SettingMyLocationViewModel(
    private val getAddressUseCase: GetAddressUseCase,
) : BaseViewModel(), SettingMyLocationActionHandler {

    private val _address: MutableLiveData<Address> = MutableLiveData()
    val address: LiveData<Address> get() = _address

    fun updateAddress(address: Address) {
        _address.value = address
    }

    companion object {
        fun factory(
            getAddressUseCase: GetAddressUseCase,
        ): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                SettingMyLocationViewModel(
                    getAddressUseCase = getAddressUseCase
                )
            }
        }
    }
}
