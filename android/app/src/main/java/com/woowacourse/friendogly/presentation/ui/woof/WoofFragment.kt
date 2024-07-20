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
        setUpMap(naverMap)
    }

    private fun initDataBinding() {
        binding.actionHandler = this
    }

    private fun initObserve() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            markNearFootPrints(footPrints = state.nearFootPrints)
        }
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

    private fun moveCameraCenterPosition(latLng: LatLng) {
        val cameraUpdate = CameraUpdate.scrollTo(latLng)
        map.moveCamera(cameraUpdate)
    }

    private fun createMarker(
        latLng: LatLng,
        isMine: Boolean,
    ) {
        val marker = Marker()
        marker.position = latLng
        marker.icon =
            if (isMine) {
                OverlayImage.fromResource(
                    R.drawable.ic_my_foot_print,
                )
            } else {
                OverlayImage.fromResource(R.drawable.ic_other_foot_print)
            }
        marker.width = MARKER_WIDTH
        marker.height = MARKER_HEIGHT
        marker.map = map

        showFootPrintInfo(marker)
    }

    private fun showFootPrintInfo(marker: Marker) {
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
            createMarker(latLng = footPrint.latLng, isMine = footPrint.isMine)
        }
    }

    override fun markFootPrint() {
        if (hasNotLocationPermissions()) return

        if (locationSource.lastLocation != null) {
            val lastLocation = locationSource.lastLocation ?: return
            val latLng =
                LatLng(
                    lastLocation.latitude,
                    lastLocation.longitude,
                )
            moveCameraCenterPosition(latLng = latLng)
            createMarker(latLng = latLng, isMine = true)
            viewModel.loadNearFootPrints(latLng = latLng)
        }
    }

    override fun changeLocationTrackingMode() {
        if (hasNotLocationPermissions()) return

        map.locationTrackingMode =
            if (map.locationTrackingMode == LocationTrackingMode.Follow) LocationTrackingMode.Face else LocationTrackingMode.Follow
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
