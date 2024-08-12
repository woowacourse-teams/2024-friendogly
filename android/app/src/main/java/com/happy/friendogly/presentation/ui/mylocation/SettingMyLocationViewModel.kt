package com.happy.friendogly.presentation.ui.mylocation

import android.location.Address
import android.location.Geocoder
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

    private val _userAddress: MutableLiveData<UserAddress> = MutableLiveData()
    val userAddress: LiveData<UserAddress> get() = _userAddress

    private val addressList: MutableSet<String?> = mutableSetOf()

    fun updateAddress(address: Address) =
        runCatching {
            addressList.clear()
            val addressLine = address.getAddressLine(0)

            val adminArea = loadAdmin(address, addressLine)
            val locality = loadLocality(address, addressLine)
            val thoroughfare = loadThoroughfare(address, addressLine)

            makeUserAddress(adminArea, locality, thoroughfare)
        }
            .onSuccess { newUserAddress ->
                _userAddress.value = newUserAddress
            }
            .onFailure {
                submitInValidLocation()
            }

    private fun loadAdmin(
        address: Address,
        addressLine: String,
    ): String {
        val admin = address.adminArea
            ?: address.adminArea
            ?: findAdminAddress(addressLine)
        addressList.add(admin)
        return admin
    }

    private fun loadLocality(
        address: Address,
        addressLine: String,
    ): String? {
        val locality = address.locality
            ?: address.subLocality
            ?: findAddressElement(
                addressLine,
                LOCALITY_SPLIT,
            )
        addressList.add(locality)
        return locality
    }

    private fun loadThoroughfare(
        address: Address,
        addressLine: String,
    ): String? {
        val thoroughfare = address.thoroughfare
            ?: address.subThoroughfare
            ?: findAddressElement(
                addressLine,
                THOROUGH_FARE_SPLIT,
            )
        addressList.add(thoroughfare)
        return thoroughfare
    }

    private fun makeUserAddress(
        adminArea: String,
        locality: String?,
        thoroughfare: String?,
    ): UserAddress {
        return UserAddress(
            adminArea = adminArea,
            subLocality = locality,
            thoroughfare = thoroughfare,
        )
    }

    override fun closeSelect() {
        _event.emit(SettingMyLocationEvent.Navigation.NavigateToPrev)
    }

    override fun submitSelect() {
        val address = userAddress.value ?: return submitInValidLocation()
        saveLocation(address)
    }

    private fun saveLocation(userAddress: UserAddress) =
        viewModelScope.launch {
            saveAddressUseCase.invoke(userAddress = userAddress)
                .onSuccess {
                    _event.emit(SettingMyLocationEvent.Navigation.NavigateToPrevWithReload)
                }
                .onFailure {
                    submitInValidLocation()
                }
        }

    private fun submitInValidLocation() {
        _event.emit(SettingMyLocationEvent.InvalidLocation)
    }

    fun saveLowLevelSdkAddress(
        geocoder: Geocoder,
        latitude: Double,
        longitude: Double,
    ) = viewModelScope.launch {
        runCatching {
            geocoder.getFromLocation(latitude, longitude, 1)
                ?.first()
                ?: return@launch submitInValidLocation()
        }
            .onSuccess { address ->
                updateAddress(address)
            }
            .onFailure {
                submitInValidLocation()
            }
    }

    private fun findAddressElement(
        addressLine: String,
        delimiterChar: String,
    ): String? {
        val addressElements = addressLine.split(ADDRESS_LINE_SPLIT)

        val delimiterChars = delimiterChar.toSet()

        return addressElements.firstOrNull { element ->
            isValidAddressElements(delimiterChars, element)
        }
    }

    private fun findAdminAddress(addressLine: String): String {
        val addressElements = addressLine.split(ADDRESS_LINE_SPLIT)

        val delimiterStrings = ADMIN_SPLIT.split(ADDRESS_LINE_SPLIT).toSet()

        return addressElements.first { element ->
            delimiterStrings.any { delimiter -> delimiter in element }
        }
    }

    private fun isValidAddressElements(
        delimiterChars: Set<Char>,
        element: String,
    ): Boolean {
        return element.last() in delimiterChars && !addressList.contains(element)
    }

    companion object {
        fun factory(saveAddressUseCase: SaveAddressUseCase): ViewModelProvider.Factory {
            return BaseViewModelFactory {
                SettingMyLocationViewModel(
                    saveAddressUseCase = saveAddressUseCase,
                )
            }
        }

        private const val ADDRESS_LINE_SPLIT = " "
        private const val ADMIN_SPLIT = "도 특별시 자치시"
        private const val LOCALITY_SPLIT = "시군구읍면"
        private const val THOROUGH_FARE_SPLIT = "읍면동리로"
    }
}
