package com.woowacourse.friendogly.presentation.ui.woof

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.databinding.FragmentWoofBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.ui.MainActivity.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.woowacourse.friendogly.presentation.ui.woof.footprint.FootPrintBottomSheet

class WoofFragment : BaseFragment<FragmentWoofBinding>(R.layout.fragment_woof), OnMapReadyCallback {
    private lateinit var map: NaverMap
    private val mapView: MapView by lazy { binding.mapView }
    private val permissionRequester: WoofPermissionRequester by lazy {
        WoofPermissionRequester(
            requireActivity(),
        )
    }
    private val locationSource: FusedLocationSource by lazy {
        FusedLocationSource(
            this,
            LOCATION_PERMISSION_REQUEST_CODE,
        )
    }

    override fun initViewCreated() {
        mapView.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        setUpMap(naverMap)
        setUpMarkBtnClickAction()
        setUpLocationBtnClickAction()
    }

    private fun setUpMap(naverMap: NaverMap) {
        map = naverMap
        map.locationSource = locationSource
        map.locationTrackingMode = LocationTrackingMode.Follow
        map.uiSettings.apply {
            isLocationButtonEnabled = true
            isCompassEnabled = false
            isZoomControlEnabled = false
            isScaleBarEnabled = false
        }
        map.locationOverlay.circleRadius = MAP_CIRCLE_RADIUS
    }

    private fun setUpLocationBtnClickAction() {
        binding.btnWoofLocation.setOnClickListener {
            if (hasNotLocationPermissions()) return@setOnClickListener

            map.locationTrackingMode =
                if (map.locationTrackingMode == LocationTrackingMode.Follow) LocationTrackingMode.Face else LocationTrackingMode.Follow
        }
    }

    private fun setUpMarkBtnClickAction() {
        binding.btnMapMark.setOnClickListener {
            if (hasNotLocationPermissions()) return@setOnClickListener

            moveCameraCenterPosition()
            createMarker()
        }
    }

    private fun hasNotLocationPermissions(): Boolean {
        if (permissionRequester.hasNotLocationPermissions()) {
            permissionRequester.checkLocationPermissions {
                makeSettingSnackbar()
            }
            return true
        }
        return false
    }

    private fun makeSettingSnackbar() {
        showSnackbar(resources.getString(R.string.woof_permission)) {
            setAction(resources.getString(R.string.woof_setting)) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data =
                    Uri.parse(
                        String.format(
                            resources.getString(
                                R.string.woof_package,
                                requireContext().packageName,
                            ),
                        ),
                    )
                startActivity(intent)
            }
        }
    }

    private fun moveCameraCenterPosition() {
        if (locationSource.lastLocation != null) {
            val lastLocation = locationSource.lastLocation ?: return
            val latLng =
                LatLng(
                    lastLocation.latitude,
                    lastLocation.longitude,
                )
            val cameraUpdate = CameraUpdate.scrollTo(latLng)
            map.moveCamera(cameraUpdate)
        }
    }

    private fun createMarker() {
        val marker = Marker()
        val position = locationSource.lastLocation ?: return
        marker.position = LatLng(position.latitude, position.longitude)
        marker.icon = OverlayImage.fromResource(R.drawable.ic_marker)
        marker.width = MARKER_WIDTH
        marker.height = MARKER_HEIGHT
        marker.map = map

        marker.setOnClickListener {
            val bottomSheet =
                FootPrintBottomSheet.newInstance(
                    memberId = 1L,
                )
            bottomSheet.show(parentFragmentManager, tag)
            true
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

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private const val MARKER_WIDTH = 125
        private const val MARKER_HEIGHT = 160
        private const val MAP_CIRCLE_RADIUS = 1000
    }
}
