package com.happy.friendogly.presentation.ui.mylocation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.happy.friendogly.domain.model.Address
import com.happy.friendogly.presentation.base.BaseViewModel

class SettingMyLocationViewModel : BaseViewModel(), SettingMyLocationActionHandler {

    private val _address: MutableLiveData<Address> = MutableLiveData()
    val address: LiveData<Address> get() = _address

    fun updateAddress(address: Address){
        _address.value = address
    }
}
