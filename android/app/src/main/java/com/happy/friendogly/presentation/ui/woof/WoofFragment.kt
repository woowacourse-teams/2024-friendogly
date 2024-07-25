package com.happy.friendogly.presentation.ui.woof

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.happy.friendogly.R
import com.happy.friendogly.application.di.AppModule
import com.happy.friendogly.databinding.FragmentWoofBinding
import com.happy.friendogly.presentation.base.BaseFragment
import com.happy.friendogly.presentation.base.observeEvent
import com.happy.friendogly.presentation.model.FootprintUiModel
import com.happy.friendogly.presentation.ui.MainActivity.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.happy.friendogly.presentation.ui.permission.LocationPermission
import com.happy.friendogly.presentation.ui.woof.footprint.FootprintBottomSheet
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
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.Duration

class WoofFragment : BaseFragment<FragmentWoofBinding>(R.layout.fragment_woof), OnMapReadyCallback {
    private lateinit var map: NaverMap
    private lateinit var latLng: LatLng
    private val mapView: MapView by lazy { binding.mapView }
    private val circleOverlay: CircleOverlay by lazy { CircleOverlay() }
    private val locationPermission: LocationPermission = initLocationPermission()

    private val locationSource: FusedLocationSource by lazy {
        FusedLocationSource(
            this,
            LOCATION_PERMISSION_REQUEST_CODE,
        )
    }
    private val viewModel by viewModels<WoofViewModel> {
        WoofViewModel.factory(
            postFootprintUseCase = AppModule.getInstance().postFootprintUseCase,
            getNearFootprintsUseCase = AppModule.getInstance().getNearFootprintsUseCase,
            getFootprintMarkBtnInfoUseCase = AppModule.getInstance().getFootprintMarkBtnInfoUseCase,
            getLandMarksUseCase = AppModule.getInstance().getLandMarksUseCase,
        )
    }

    override fun initViewCreated() {
        binding.layoutWoofLoading.isVisible = true
        binding.lottieWoofLoading.playAnimation()
        initDataBinding()
        initObserve()
        mapView.getMapAsync(this)
        clickMarkBtn()
        clickLocationBtn()
    }

    private fun clickMarkBtn() {
        binding.btnWoofMark.setOnClickListener {
            if (!locationPermission.hasPermissions()) {
                locationPermission.createAlarmDialog().show(parentFragmentManager, "TAG")
            } else {
                viewModel.markFootprint(latLng)
            }
        }
    }

    private fun clickLocationBtn() {
        binding.btnWoofLocation.setOnClickListener {
            if (!locationPermission.hasPermissions()) {
                locationPermission.createAlarmDialog().show(parentFragmentManager, "TAG")
            } else {
                viewModel.changeLocationTrackingMode()
            }
        }
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
            val footprintSave = state.footprintSave ?: return@observe
            markNearFootPrints(footPrints = state.nearFootprints)
            createMarker(
                footprintId = footprintSave.footprintId,
                createdAt = footprintSave.createdAt,
                latLng = latLng,
                isMine = true,
            )
            moveCameraCenterPosition()
        }

        viewModel.mapActions.observeEvent(viewLifecycleOwner) { event ->
            when (event) {
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
                is WoofSnackbarActions.ShowHasNotPetSnackbar ->
                    showSnackbar(
                        String.format(
                            resources.getString(
                                R.string.woof_has_not_pet,
                            ),
                        ),
                    )

                is WoofSnackbarActions.ShowCantClickMarkBtnSnackbar -> {
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

    private fun initLocationPermission() =
        LocationPermission.from(this) { isPermitted ->
            if (isPermitted) {
                activateMap()
            } else {
                showSnackbar(getString(R.string.permission_denied_message))
                map.locationTrackingMode =
                    LocationTrackingMode.NoFollow
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
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    binding.layoutWoofLoading.isVisible = false
                    // binding.lottieWoofLoading.pauseAnimation()
                },
                1000,
            )
        }
    }

    private fun setUpCircleOverlay() {
        circleOverlay.center = latLng
        circleOverlay.radius = MAP_CIRCLE_RADIUS
        circleOverlay.color = resources.getColor(R.color.map_circle, null)
        circleOverlay.map = map
    }

    private fun moveCameraCenterPosition() {
        val cameraUpdate = CameraUpdate.scrollAndZoomTo(latLng, DEFAULT_ZOOM)
        map.moveCamera(cameraUpdate)
        map.locationTrackingMode = LocationTrackingMode.Follow
        setUpCircleOverlay()
    }

    private fun createMarker(
        footprintId: Long,
        createdAt: LocalDateTime,
        latLng: LatLng,
        isMine: Boolean,
    ) {
        val iconImage = if (isMine) R.drawable.ic_my_footprint else R.drawable.ic_other_footprint
        val marker = Marker()
        marker.position = latLng
        marker.icon = OverlayImage.fromResource(iconImage)
        marker.width = MARKER_WIDTH
        marker.height = MARKER_HEIGHT
        marker.zIndex = createdAt.toZIndex()
        marker.map = map

        setUpMarkerAction(footprintId, marker)
    }

    private fun LocalDateTime.toZIndex(): Int {
        val duration = Duration.between(this.toJavaLocalDateTime(), java.time.LocalDateTime.now())
        return Int.MAX_VALUE - (duration.toHours() * 100000000 + duration.toMinutes() * 1000000 + duration.toMillis()).toInt()
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
                createdAt = footPrint.createdAt,
                latLng = LatLng(footPrint.latitude, footPrint.longitude),
                isMine = footPrint.isMine,
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
        private const val MAX_ZOOM = 20.0
    }
}
