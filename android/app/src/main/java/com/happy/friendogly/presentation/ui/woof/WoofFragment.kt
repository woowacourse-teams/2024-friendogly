package com.happy.friendogly.presentation.ui.woof

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.viewModels
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.CameraUpdate.REASON_GESTURE
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.CircleOverlay
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.FragmentWoofBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.model.FootprintUiModel
import com.happy.friendogly.presentation.ui.MainActivity.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.happy.friendogly.presentation.ui.woof.footprint.FootprintBottomSheet

class WoofFragment :
    BaseFragment<FragmentWoofBinding>(R.layout.fragment_woof), OnMapReadyCallback {
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
            permissionRequester = permissionRequester,
            postFootprintUseCase = AppModule.getInstance().postFootprintUseCase,
            getNearFootprintsUseCase = AppModule.getInstance().getNearFootprintsUseCase,
            getFootprintMarkBtnInfoUseCase = AppModule.getInstance().getFootprintMarkBtnInfoUseCase,
            getLandMarksUseCase = AppModule.getInstance().getLandMarksUseCase,
        )
    }

    override fun initViewCreated() {
        showProgressBar()
        initDataBinding()
        initObserve()
        mapView.getMapAsync(this)
    }

    override fun onMapReady(naverMap: NaverMap) {
        initMap(naverMap)
        activateMap()
    }

    private fun initDataBinding() {
        binding.vm = viewModel
    }

    private fun initObserve() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            val footprintId = state.createdFootprintId ?: return@observe
            markNearFootPrints(footPrints = state.nearFootprints)
            createMarker(footprintId = footprintId, latLng = latLng, isMine = true)
            moveCameraCenterPosition()
        }

        viewModel.mapActions.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is WoofMapActions.MarkFootPrint -> viewModel.markFootprint(latLng)
                is WoofMapActions.ChangeMapToNoFollowTrackingMode ->
                    map.locationTrackingMode =
                        LocationTrackingMode.NoFollow

                is WoofMapActions.ChangeMapToFollowTrackingMode ->
                    map.locationTrackingMode =
                        LocationTrackingMode.Follow

                is WoofMapActions.ChangeMapToFaceTrackingMode ->
                    map.locationTrackingMode =
                        LocationTrackingMode.Face
            }
        }

        viewModel.snackbarActions.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
                is WoofSnackbarActions.ShowSettingSnackbar -> showSettingSnackbar()
                is WoofSnackbarActions.ShowCantMarkSnackbar -> {
                    showSnackbar(
                        String.format(
                            resources.getString(
                                R.string.woof_cant_mark,
                                event.remainingTime,
                            ),
                        ),
                    )
                }
            }
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

        map.addOnCameraChangeListener { reason, _ ->
            if (reason == REASON_GESTURE) {
                viewModel.changeMapTrackingModeToNoFollow()
            }
        }
    }

    private fun activateMap() {
        locationSource.activate { location ->
            val lastLocation = location ?: return@activate
            latLng = LatLng(lastLocation.latitude, lastLocation.longitude)
            moveCameraCenterPosition()
            hideProgressBar()
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
        footprintId: Long,
        latLng: LatLng,
        isMine: Boolean,
    ) {
        val iconImage = if (isMine) R.drawable.ic_my_footprint else R.drawable.ic_other_footprint
        val marker = Marker()
        marker.position = latLng
        marker.icon = OverlayImage.fromResource(iconImage)
        marker.width = MARKER_WIDTH
        marker.height = MARKER_HEIGHT
        marker.map = map

        setUpMarkerAction(footprintId, marker)
    }

    private fun setUpMarkerAction(
        footprintId: Long,
        marker: Marker,
    ) {
        marker.setOnClickListener {
            val bottomSheet =
                FootprintBottomSheet.newInstance(
                    footPrintId = footprintId,
                )
            bottomSheet.show(parentFragmentManager, tag)
            true
        }
    }

    private fun markNearFootPrints(footPrints: List<FootprintUiModel>) {
        footPrints.forEach { footPrint ->
            createMarker(
                footprintId = footPrint.footprintId,
                latLng = LatLng(footPrint.latitude, footPrint.longitude),
                isMine = footPrint.isMine,
            )
        }
    }

    private fun showProgressBar() {
        binding.layoutWoofProgressBar.visibility = View.VISIBLE
        animateProgressBar(true)
    }

    private fun hideProgressBar() {
        binding.layoutWoofProgressBar.visibility = View.GONE
        animateProgressBar(false)
    }

    private fun animateProgressBar(show: Boolean) {
        val fadeInDuration = 500L
        val fadeOutDuration = 500L

        val fadeIn = AlphaAnimation(0.2f, 0.8f)
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = fadeInDuration
        fadeIn.setAnimationListener(
            object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {
                    binding.ivWoofProgressBar.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animation) {
                    val fadeOut = AlphaAnimation(0.8f, 0.2f)
                    fadeOut.interpolator = AccelerateInterpolator()
                    fadeOut.duration = fadeOutDuration
                    fadeOut.setAnimationListener(
                        object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation) {}

                            override fun onAnimationEnd(animation: Animation) {
                                binding.ivWoofProgressBar.visibility = View.GONE
                                if (show) {
                                    binding.ivWoofProgressBar.startAnimation(fadeIn)
                                }
                            }

                            override fun onAnimationRepeat(animation: Animation) {}
                        },
                    )

                    binding.ivWoofProgressBar.startAnimation(fadeOut)
                }

                override fun onAnimationRepeat(animation: Animation) {}
            },
        )
        binding.ivWoofProgressBar.startAnimation(fadeIn)
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
