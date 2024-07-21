package com.woowacourse.friendogly.presentation.ui.woof

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.viewModels
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.woowacourse.friendogly.R
import com.woowacourse.friendogly.application.FriendoglyApplication.Companion.remoteWoofDataSource
import com.woowacourse.friendogly.data.repository.WoofRepositoryImpl
import com.woowacourse.friendogly.databinding.FragmentWoofBinding
import com.woowacourse.friendogly.presentation.base.BaseFragment
import com.woowacourse.friendogly.presentation.model.FootPrintUiModel
import com.woowacourse.friendogly.presentation.ui.MainActivity.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.woowacourse.friendogly.presentation.ui.woof.footprint.FootPrintBottomSheet

class WoofFragment :
    BaseFragment<FragmentWoofBinding>(R.layout.fragment_woof),
    OnMapReadyCallback,
    WoofActionHandler {
    private lateinit var map: NaverMap
    private lateinit var latLng: LatLng
    private val mapView: MapView by lazy { binding.mapView }
    private val circleOverlay: CircleOverlay by lazy { CircleOverlay() }
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
    private val viewModel by viewModels<WoofViewModel> {
        WoofViewModel.factory(
            woofRepository =
                WoofRepositoryImpl(
                    remoteWoofDataSource,
                ),
        )
    }

    override fun initViewCreated() {
        initDataBinding()
        initObserve()
        mapView.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        initMap(naverMap)
        activateMap()
    }

    private fun initDataBinding() {
        binding.actionHandler = this
    }

    private fun initObserve() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            markNearFootPrints(footPrints = state.nearFootPrints)
            createMarker(latLng = latLng, isMine = true)
            moveCameraCenterPosition()
        }
    }

    private fun initMap(naverMap: NaverMap) {
        map = naverMap
        map.minZoom = MIN_ZOOM
        map.maxZoom = MAX_ZOOM
        map.locationSource = locationSource

        map.uiSettings.apply {
            isLocationButtonEnabled = true
            isCompassEnabled = true
            isZoomControlEnabled = false
            isScaleBarEnabled = false
        }

        map.addOnLocationChangeListener { location ->
            latLng = LatLng(location.latitude, location.longitude)
            circleOverlay.center = latLng
        }
    }

    private fun activateMap() {
        locationSource.activate { location ->
            val lastLocation = location ?: return@activate
            latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
            moveCameraCenterPosition()
        }
    }

    private fun setUpCircleOverlay() {
        circleOverlay.center = latLng
        circleOverlay.radius = MAP_CIRCLE_RADIUS
        circleOverlay.color = resources.getColor(R.color.map_circle, null)
        circleOverlay.map = map
    }

    private fun showSettingSnackbar() {
        permissionRequester.checkLocationPermissions {
            showSnackbar(resources.getString(R.string.woof_permission)) {
                setAction(resources.getString(R.string.woof_setting)) {
                    val packageName =
                        String.format(
                            resources.getString(
                                R.string.woof_package,
                                requireContext().packageName,
                            ),
                        )
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.data = Uri.parse(packageName)
                    startActivity(intent)
                }
            }
        }
    }

    private fun moveCameraCenterPosition() {
        val cameraUpdate = CameraUpdate.scrollAndZoomTo(latLng, DEFAULT_ZOOM)
        map.moveCamera(cameraUpdate)
        map.locationTrackingMode = LocationTrackingMode.Follow
        setUpCircleOverlay()
    }

    private fun createMarker(
        latLng: LatLng,
        isMine: Boolean,
    ) {
        val iconImage = if (isMine) R.drawable.ic_my_foot_print else R.drawable.ic_other_foot_print
        val marker = Marker()
        marker.position = latLng
        marker.icon = OverlayImage.fromResource(iconImage)
        marker.width = MARKER_WIDTH
        marker.height = MARKER_HEIGHT
        marker.map = map

        setUpMarkerAction(marker)
    }

    private fun setUpMarkerAction(marker: Marker) {
        marker.setOnClickListener {
            val bottomSheet =
                FootPrintBottomSheet.newInstance(
                    memberId = 1L,
                )
            bottomSheet.show(parentFragmentManager, tag)
            true
        }
    }

    private fun markNearFootPrints(footPrints: List<FootPrintUiModel>) {
        footPrints.forEach { footPrint ->
            createMarker(
                latLng = LatLng(footPrint.latitude, footPrint.longitude),
                isMine = footPrint.isMine,
            )
        }
    }

    override fun markFootPrint() {
        if (permissionRequester.hasLocationPermissions()) {
            if (locationSource.lastLocation != null) {
                val lastLocation = locationSource.lastLocation ?: return
                latLng =
                    LatLng(
                        lastLocation.latitude,
                        lastLocation.longitude,
                    )
                viewModel.markFootPrint(latLng = latLng)
                viewModel.loadNearFootPrints(latLng = latLng)
            }
        } else {
            showSettingSnackbar()
        }
    }

    override fun changeLocationTrackingMode() {
        if (permissionRequester.hasLocationPermissions()) {
            map.locationTrackingMode =
                if (map.locationTrackingMode == LocationTrackingMode.Follow) LocationTrackingMode.Face else LocationTrackingMode.Follow
        } else {
            map.locationTrackingMode = LocationTrackingMode.None
            showSettingSnackbar()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissionRequester.hasLocationPermissions()) {
            activateMap()
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
        private const val MAP_CIRCLE_RADIUS = 1000.0
        private const val MIN_ZOOM = 10.0
        private const val DEFAULT_ZOOM = 13.5
        private const val MAX_ZOOM = 17.0
    }
}
