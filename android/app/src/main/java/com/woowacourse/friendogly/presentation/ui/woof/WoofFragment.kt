package com.woowacourse.friendogly.presentation.ui.woof

import android.os.Bundle
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
    private lateinit var locationSource: FusedLocationSource
    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap

    override fun initViewCreated() {
        setUpDataBinding()
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        mapView = binding.mapView
//        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        setUpNaverMap(naverMap)
        setUpMarkBtnClickAction()
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

    private fun setUpDataBinding() {
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun setUpNaverMap(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        naverMap.uiSettings.apply {
            isLocationButtonEnabled = true
            isCompassEnabled = false
            isZoomControlEnabled = false
            isScaleBarEnabled = false
        }
    }

    private fun setUpMarkBtnClickAction() {
        binding.btnMapMark.setOnClickListener {
            moveCameraCenterPosition()
            createMarker()
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
            naverMap.moveCamera(cameraUpdate)
        }
    }

    private fun createMarker() {
        val marker = Marker()
        val position = locationSource.lastLocation ?: return
        marker.position = LatLng(position.latitude, position.longitude)
        marker.icon = OverlayImage.fromResource(R.drawable.ic_marker)
        marker.width = 125
        marker.height = 160
        marker.map = naverMap

        marker.setOnClickListener {
            val bottomSheet =
                FootPrintBottomSheet.newInstance(
                    memberId = 1L,
                )
            bottomSheet.show(parentFragmentManager, tag)
            true
        }
    }
}
