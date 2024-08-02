package com.happy.friendogly.presentation.ui.mylocation

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.location.LocationServices
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.ActivitySettingMyLocationBinding
import com.happy.friendogly.presentation.base.BaseActivity
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.ui.MainActivity
import com.happy.friendogly.presentation.ui.permission.LocationPermission
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import java.util.Locale

class SettingMyLocationActivity :
    BaseActivity<ActivitySettingMyLocationBinding>(R.layout.activity_setting_my_location),
    OnMapReadyCallback {
    private lateinit var map: NaverMap
    private val mapView: MapView by lazy { binding.mapViewMyLocation }
    private val loadingView: LottieAnimationView by lazy { binding.lottieMyLocationLoading }
    private val locationPermission: LocationPermission = initLocationPermission()

    private val viewModel: SettingMyLocationViewModel by viewModels<SettingMyLocationViewModel> {
        SettingMyLocationViewModel.factory(
            saveAddressUseCase = AppModule.getInstance().saveAddressUseCase
        )
    }

    private val locationSource: FusedLocationSource by lazy {
        FusedLocationSource(
            this,
            MainActivity.LOCATION_PERMISSION_REQUEST_CODE,
        )
    }

    override fun initCreateView() {
        requestUserPermission()
        initDataBinding()
        initObserver()
        startAnimation()
        mapView.getMapAsync(this)
    }

    private fun requestUserPermission() {
        if (!locationPermission.hasPermissions()) {
            locationPermission.createAlarmDialog().show(supportFragmentManager, "TAG")
        }
    }

    private fun startAnimation() {
        loadingView.playAnimation()
    }

    private fun cancelAnimation() {
        binding.lottieMyLocationLoading.isVisible = false
        binding.lottieMyLocationLoading.cancelAnimation()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initObserver() {
        viewModel.event.observeEvent(this){ event ->
            when(event){
                SettingMyLocationEvent.InvalidLocation -> showSnackbar(getString(R.string.my_location_submit_fail))
                SettingMyLocationEvent.Navigation.NavigateToPrev -> finish()
            }
        }
    }

    private fun initLocationPermission() =
        LocationPermission.from(this) { isPermitted ->
            if (isPermitted) {
                mapView.getMapAsync(this)
                activateMap()
            } else {
                showSnackbar(getString(R.string.permission_denied_message))
                map.locationTrackingMode =
                    LocationTrackingMode.NoFollow
            }
        }

    private fun activateMap() {
        locationSource.activate { location ->
            val lastLocation = location ?: return@activate
            val latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
            moveCameraCenterPosition(latLng)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    cancelAnimation()
                    initMarker(latLng)
                    loadAddress(latLng)
                },
                1000
            )
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        initMap(naverMap)
        activateMap()
    }

    private fun moveCameraCenterPosition(latLng: LatLng) {
        val cameraUpdate = CameraUpdate.scrollAndZoomTo(latLng, DEFAULT_ZOOM)
        map.moveCamera(cameraUpdate)
        map.locationTrackingMode = LocationTrackingMode.Follow
    }

    private fun initMarker(latLng: LatLng) {
        val marker = Marker()
        marker.position = latLng
        marker.icon = OverlayImage.fromResource(R.drawable.ic_marker_mine_clicked)
        marker.width = MARKER_WIDTH
        marker.height = MARKER_HEIGHT
        marker.map = map
    }

    private fun initMap(map: NaverMap) {
        this.map = map
        map.locationSource = locationSource
        map.uiSettings.apply {
            isScrollGesturesEnabled = false
            isZoomGesturesEnabled = false
            isTiltGesturesEnabled = false
            isRotateGesturesEnabled = false
        }
    }

    private fun loadAddress(latLng: LatLng){
        val geocoder = Geocoder(this, Locale.KOREA)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1) { addressList->
                saveAddress(addressList[0])
            }
        } else {
            val addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                ?: return showSnackbar(getString(R.string.my_location_load_fail))
            saveAddress(addressList[0])
        }
    }

    private fun saveAddress(address: Address){
        with(address) {
            viewModel.updateAddress(
                adminArea = adminArea,
                subLocality = subLocality,
                thoroughfare = thoroughfare,
            )
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private const val DEFAULT_ZOOM = 13.5
        private const val MARKER_WIDTH = 24 * 5
        private const val MARKER_HEIGHT = 37 * 5

        fun getIntent(context: Context): Intent {
            return Intent(context, SettingMyLocationActivity::class.java)
        }
    }
}
